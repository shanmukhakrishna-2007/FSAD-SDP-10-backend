package com.careercompass.ai.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Centralized security event logger.
 * Tracks failed login attempts, suspicious activity, and account lockouts.
 * In production this would forward to a SIEM / alerting system.
 */
@Component
public class SecurityEventLogger {

    private static final Logger log = LoggerFactory.getLogger("SECURITY_AUDIT");

    public void loginSuccess(String email, String ip) {
        log.info("[LOGIN_SUCCESS] email={} ip={}", mask(email), ip);
    }

    public void loginFailed(String email, String ip, int attemptCount) {
        log.warn("[LOGIN_FAILED] email={} ip={} attempt={}", mask(email), ip, attemptCount);
    }

    public void accountLocked(String email, int minutes) {
        log.warn("[ACCOUNT_LOCKED] email={} lockedForMinutes={}", mask(email), minutes);
    }

    public void accountUnlocked(String email) {
        log.info("[ACCOUNT_UNLOCKED] email={}", mask(email));
    }

    public void registrationAttempt(String email) {
        log.info("[REGISTER_ATTEMPT] email={}", mask(email));
    }

    public void emailVerified(String email) {
        log.info("[EMAIL_VERIFIED] email={}", mask(email));
    }

    public void passwordResetRequested(String email) {
        log.info("[PASSWORD_RESET_REQ] email={}", mask(email));
    }

    public void passwordResetCompleted(String email) {
        log.info("[PASSWORD_RESET_DONE] email={}", mask(email));
    }

    public void mfaChallengeSent(String email) {
        log.info("[MFA_CHALLENGE] email={}", mask(email));
    }

    public void mfaVerified(String email) {
        log.info("[MFA_VERIFIED] email={}", mask(email));
    }

    public void mfaFailed(String email) {
        log.warn("[MFA_FAILED] email={}", mask(email));
    }

    public void suspiciousActivity(String email, String reason) {
        log.error("[SUSPICIOUS] email={} reason={}", mask(email), reason);
    }

    /** Mask email for GDPR-safe logging: "john@example.com" → "j***@e***.com" */
    private String mask(String email) {
        if (email == null || !email.contains("@")) return "***";
        String[] parts = email.split("@");
        String local = parts[0].length() > 1 ? parts[0].charAt(0) + "***" : "***";
        String[] domainParts = parts[1].split("\\.");
        String domain = domainParts[0].length() > 1 ? domainParts[0].charAt(0) + "***" : "***";
        return local + "@" + domain + "." + domainParts[domainParts.length - 1];
    }
}
