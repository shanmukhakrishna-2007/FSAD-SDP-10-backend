package com.careercompass.ai.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer xp = 0;
    private Integer level = 1;

    @Column(length = 500)
    private String refreshToken;

    private LocalDateTime createdAt;

    private boolean emailVerified = true;

    @Column(length = 64)
    private String verificationToken;

    private int failedLoginAttempts = 0;

    private LocalDateTime accountLockedUntil;

    private boolean mfaEnabled = false;

    @Column(length = 6)
    private String mfaSecret;

    @Column(length = 64)
    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;

    private long tokenVersion = 0;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // ── UserDetails contract ──

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        if (accountLockedUntil == null) return true;
        return LocalDateTime.now().isAfter(accountLockedUntil);
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // ── Getters & Setters ──

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Integer getXp() { return xp; }
    public void setXp(Integer xp) { this.xp = xp; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    public LocalDateTime getAccountLockedUntil() { return accountLockedUntil; }
    public void setAccountLockedUntil(LocalDateTime accountLockedUntil) { this.accountLockedUntil = accountLockedUntil; }
    public boolean isMfaEnabled() { return mfaEnabled; }
    public void setMfaEnabled(boolean mfaEnabled) { this.mfaEnabled = mfaEnabled; }
    public String getMfaSecret() { return mfaSecret; }
    public void setMfaSecret(String mfaSecret) { this.mfaSecret = mfaSecret; }
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }
    public LocalDateTime getResetPasswordTokenExpiry() { return resetPasswordTokenExpiry; }
    public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) { this.resetPasswordTokenExpiry = resetPasswordTokenExpiry; }
    public long getTokenVersion() { return tokenVersion; }
    public void setTokenVersion(long tokenVersion) { this.tokenVersion = tokenVersion; }

    // ── Builder ──

    public static UserBuilder builder() { return new UserBuilder(); }
    public static class UserBuilder {
        private String name, email, password, mfaSecret, verificationToken, refreshToken;
        private Role role;
        private boolean emailVerified = true, mfaEnabled = false;
        private int failedLoginAttempts = 0;
        private long tokenVersion = 0;
        private Integer xp = 0, level = 1;

        public UserBuilder name(String v) { this.name = v; return this; }
        public UserBuilder email(String v) { this.email = v; return this; }
        public UserBuilder password(String v) { this.password = v; return this; }
        public UserBuilder role(Role v) { this.role = v; return this; }
        public UserBuilder emailVerified(boolean v) { this.emailVerified = v; return this; }
        public UserBuilder mfaEnabled(boolean v) { this.mfaEnabled = v; return this; }
        public UserBuilder mfaSecret(String v) { this.mfaSecret = v; return this; }
        public UserBuilder verificationToken(String v) { this.verificationToken = v; return this; }
        public UserBuilder failedLoginAttempts(int v) { this.failedLoginAttempts = v; return this; }
        public UserBuilder tokenVersion(long v) { this.tokenVersion = v; return this; }
        public UserBuilder xp(Integer v) { this.xp = v; return this; }
        public UserBuilder level(Integer v) { this.level = v; return this; }
        public UserBuilder refreshToken(String v) { this.refreshToken = v; return this; }

        public User build() {
            User u = new User();
            u.name = name; u.email = email; u.password = password; u.role = role;
            u.emailVerified = emailVerified; u.mfaEnabled = mfaEnabled; u.mfaSecret = mfaSecret;
            u.verificationToken = verificationToken; u.failedLoginAttempts = failedLoginAttempts;
            u.tokenVersion = tokenVersion; u.xp = xp; u.level = level; u.refreshToken = refreshToken;
            return u;
        }
    }
}
