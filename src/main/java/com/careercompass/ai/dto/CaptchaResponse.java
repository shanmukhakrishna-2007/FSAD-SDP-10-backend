package com.careercompass.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaptchaResponse {
    private String id;
    private String imageBase64;
    private String fallbackCode;
}
