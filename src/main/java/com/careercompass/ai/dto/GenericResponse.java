package com.careercompass.ai.dto;

public class GenericResponse {
    private String message;

    public GenericResponse() {}
    public GenericResponse(String message) { this.message = message; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static GenericResponseBuilder builder() { return new GenericResponseBuilder(); }
    public static class GenericResponseBuilder {
        private String message;
        public GenericResponseBuilder message(String message) { this.message = message; return this; }
        public GenericResponse build() { return new GenericResponse(message); }
    }
}
