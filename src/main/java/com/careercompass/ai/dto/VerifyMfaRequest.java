package com.careercompass.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyMfaRequest {
    private String email;
    private String otp;

    public String getEmail() { return email; }
    public String getOtp() { return otp; }
}
