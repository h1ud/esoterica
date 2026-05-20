package com.webproject.esoteria.domain.dto;

// Respuesta del login admin con el token que protege los endpoints /api/admin.
public class AdminLoginResponse {
    private String token;
    private String tokenType;
    private long expiresInSeconds;

    public AdminLoginResponse(String token, String tokenType, long expiresInSeconds) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }
}
