package com.careercompass.ai.service;

import com.careercompass.ai.dto.*;
import com.careercompass.ai.model.Role;
import com.careercompass.ai.model.User;
import com.careercompass.ai.repository.UserRepository;
import com.careercompass.ai.security.JwtService;
import com.careercompass.ai.security.SecurityEventLogger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@SuppressWarnings("null")
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;
    private static final String GENERIC_BAD_CREDENTIALS = "Invalid credentials";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecurityEventLogger securityLog;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, SecurityEventLogger securityLog) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService; this.securityLog = securityLog;
    }

    // ── REGISTER ──────────────────────────────────────────────

    public GenericResponse register(RegisterRequest request) {
        securityLog.registrationAttempt(request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            return GenericResponse.builder()
                    .message("Account creation processed.")
                    .build();
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .emailVerified(true) // SKIP verification
                .failedLoginAttempts(0)
                .mfaEnabled(false)
                .tokenVersion(0)
                .build();

        userRepository.save(user);

        return GenericResponse.builder()
                .message("Identity initialized. You may now log in.")
                .build();
    }

    // ── VERIFY EMAIL ──────────────────────────────────────────

    public GenericResponse verifyEmail(String token) {
        var userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isEmpty()) {
            return GenericResponse.builder().message("Verification processed.").build();
        }
        User user = userOpt.get();
        user.setEmailVerified(true);
        user.setVerificationToken(null); // single-use
        userRepository.save(user);
        securityLog.emailVerified(user.getEmail());
        return GenericResponse.builder().message("Email verified successfully. You may now log in.").build();
    }

    // ── LOGIN ─────────────────────────────────────────────────

    public AuthResponse login(LoginRequest request, HttpServletRequest httpReq, HttpServletResponse httpRes) {
        String ip = httpReq.getRemoteAddr();

        var userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, GENERIC_BAD_CREDENTIALS);
        }

        User user = userOpt.get();

        if (!user.isAccountNonLocked()) {
            securityLog.suspiciousActivity(user.getEmail(), "Login attempt on locked account from ip=" + ip);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Account temporarily locked. Try again later.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleFailedLogin(user, ip);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, GENERIC_BAD_CREDENTIALS);
        }

        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
            userRepository.save(user);
        }

        if (user.isMfaEnabled()) {
            String otp = generateOtp();
            user.setMfaSecret(otp);
            userRepository.save(user);
            securityLog.mfaChallengeSent(user.getEmail());
            return AuthResponse.builder()
                    .message("MFA verification required")
                    .mfaRequired(true)
                    .build();
        }

        user.setTokenVersion(user.getTokenVersion() + 1);
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        issueRefreshCookie(refreshToken, httpRes);

        securityLog.loginSuccess(user.getEmail(), ip);

        return AuthResponse.builder()
                .message("Login successful")
                .accessToken(accessToken)
                .role(user.getRole().name())
                .mfaRequired(false)
                .build();
    }

    // ── MFA VERIFICATION ──────────────────────────────────────

    public AuthResponse verifyMfa(VerifyMfaRequest request, HttpServletResponse httpRes) {
        var userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, GENERIC_BAD_CREDENTIALS);
        }
        User user = userOpt.get();

        if (user.getMfaSecret() == null || !user.getMfaSecret().equals(request.getOtp())) {
            securityLog.mfaFailed(user.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP code");
        }

        // Clear OTP (single-use)
        user.setMfaSecret(null);
        user.setTokenVersion(user.getTokenVersion() + 1);
        
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        issueRefreshCookie(refreshToken, httpRes);
        
        securityLog.mfaVerified(user.getEmail());

        return AuthResponse.builder()
                .message("Login successful")
                .accessToken(accessToken)
                .role(user.getRole().name())
                .mfaRequired(false)
                .build();
    }

    // ── FORGOT PASSWORD ───────────────────────────────────────

    public GenericResponse forgotPassword(ForgotPasswordRequest request) {
        securityLog.passwordResetRequested(request.getEmail());

        var userOpt = userRepository.findByEmail(request.getEmail());
        // Always return same generic message
        if (userOpt.isEmpty()) {
            return GenericResponse.builder().message("If an account exists, a reset link has been sent.").build();
        }

        User user = userOpt.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(30)); // 30 min expiry
        userRepository.save(user);

        log.info("╔══════════════════════════════════════════════════════╗");
        log.info("║  PASSWORD RESET TOKEN                              ║");
        log.info("║  Email : {}                                        ", user.getEmail());
        log.info("║  Token : {}                                        ", resetToken);
        log.info("║  URL   : http://localhost:5173/auth?reset={}       ", resetToken);
        log.info("╚══════════════════════════════════════════════════════╝");

        return GenericResponse.builder().message("If an account exists, a reset link has been sent.").build();
    }

    // ── RESET PASSWORD ────────────────────────────────────────

    public GenericResponse resetPassword(ResetPasswordRequest request) {
        var userOpt = userRepository.findByResetPasswordToken(request.getToken());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired reset token.");
        }

        User user = userOpt.get();

        if (user.getResetPasswordTokenExpiry() == null ||
            LocalDateTime.now().isAfter(user.getResetPasswordTokenExpiry())) {
            // Token expired — clear it
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired reset token.");
        }

        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Invalidate token (single-use)
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        // Rotate token version to invalidate all existing sessions
        user.setTokenVersion(user.getTokenVersion() + 1);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        userRepository.save(user);

        securityLog.passwordResetCompleted(user.getEmail());
        return GenericResponse.builder().message("Password reset successfully. You may now log in.").build();
    }

    // ── LOGOUT ────────────────────────────────────────────────

    public GenericResponse logout(HttpServletResponse httpRes) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpRes.addCookie(cookie);
        return GenericResponse.builder().message("Logged out successfully").build();
    }

    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token missing");
        }

        var userOpt = userRepository.findByRefreshToken(refreshToken);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        User user = userOpt.get();
        String newAccessToken = jwtService.generateToken(user);
        
        return AuthResponse.builder()
                .message("Token refreshed")
                .accessToken(newAccessToken)
                .role(user.getRole().name())
                .build();
    }

    // ── GET CURRENT USER (for frontend session validation) ───

    public AuthResponse getCurrentUser(HttpServletRequest httpReq) {
        // The JwtAuthFilter already validated the cookie/token and set the SecurityContext.
        // The controller extracts the authenticated principal.
        // This method is just a bridge called by the controller.
        return null; // controller handles the logic directly
    }

    // ═══════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════

    private void handleFailedLogin(User user, String ip) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            securityLog.accountLocked(user.getEmail(), LOCK_DURATION_MINUTES);
        }

        securityLog.loginFailed(user.getEmail(), ip, attempts);
        userRepository.save(user);
    }

    private void issueRefreshCookie(String token, HttpServletResponse httpRes) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        httpRes.addCookie(cookie);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
