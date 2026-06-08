package com.lorenzo.gestaoconsultas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDto {

    private String token;

    @JsonProperty("requer2fa")
    private boolean requer2fa;

    public LoginResponseDto(String token, boolean requer2fa) {
        this.token = token;
        this.requer2fa = requer2fa;
    }

    public String getToken() {
        return token;
    }

    @JsonProperty("requer2fa")
    public boolean getRequer2fa() {
        return requer2fa;
    }
}
