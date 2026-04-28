package com.careercompass.ai.controller;

import com.careercompass.ai.dto.*;
import com.careercompass.ai.model.User;
import com.careercompass.ai.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    /**
     * Register a new user.
     * Returns a generic message — never reveals whether the email was taken.
     */
    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Login with email and password.
     * On success the JWT is set as an HttpOnly cookie, NOT returned in the body.
     * If MFA is enabled the response indicates mfaRequired=true and no cookie is set yet.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpReq,
            HttpServletResponse httpRes) {
        return ResponseEntity.ok(authService.login(request, httpReq, httpRes));
    }

    /**
     * Verify MFA OTP code.
     * On success the JWT is issued as an HttpOnly cookie.
     */
    @PostMapping("/verify-mfa")
    public ResponseEntity<AuthResponse> verifyMfa(
            @RequestBody VerifyMfaRequest request,
            HttpServletResponse httpRes) {
        return ResponseEntity.ok(authService.verifyMfa(request, httpRes));
    }

    /**
     * Verify email address using the token sent during registration.
     */
    @GetMapping("/verify-email")
    public ResponseEntity<GenericResponse> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    /**
     * Request a password reset link.
     * Always returns a generic message regardless of whether the email exists.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<GenericResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    /**
     * Reset password using an expiring, single-use token.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    /**
     * Logout — clears the HttpOnly JWT cookie.
     */
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(HttpServletResponse httpRes) {
        return ResponseEntity.ok(authService.logout(httpRes));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest httpReq, HttpServletResponse httpRes) {
        return ResponseEntity.ok(authService.refresh(httpReq, httpRes));
    }

    /**
     * Returns the current user's role if they have a valid JWT cookie.
     * Used by the frontend to restore session state on page reload.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(AuthResponse.builder()
                .message("Authenticated")
                .role(user.getRole().name())
                .mfaRequired(false)
                .build());
    }
}
