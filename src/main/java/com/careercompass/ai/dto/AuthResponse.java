package com.careercompass.ai.dto;

public class AuthResponse {
    private String message;
    private String accessToken;
    private String role;
    private boolean mfaRequired = false;

    public AuthResponse() {}
    public AuthResponse(String message, String accessToken, String role, boolean mfaRequired) {
        this.message = message; this.accessToken = accessToken;
        this.role = role; this.mfaRequired = mfaRequired;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isMfaRequired() { return mfaRequired; }
    public void setMfaRequired(boolean mfaRequired) { this.mfaRequired = mfaRequired; }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private String message, accessToken, role;
        private boolean mfaRequired = false;
        public AuthResponseBuilder message(String v) { this.message = v; return this; }
        public AuthResponseBuilder accessToken(String v) { this.accessToken = v; return this; }
        public AuthResponseBuilder role(String v) { this.role = v; return this; }
        public AuthResponseBuilder mfaRequired(boolean v) { this.mfaRequired = v; return this; }
        public AuthResponse build() { return new AuthResponse(message, accessToken, role, mfaRequired); }
    }
}
