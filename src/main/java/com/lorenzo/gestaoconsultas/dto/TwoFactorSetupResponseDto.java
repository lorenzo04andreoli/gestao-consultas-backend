package com.lorenzo.gestaoconsultas.dto;

public class TwoFactorSetupResponseDto {

    private String secret;
    private String qrCode;

    public TwoFactorSetupResponseDto(String secret, String qrCode) {
        this.secret = secret;
        this.qrCode = qrCode;
    }

    public String getSecret() {
        return secret;
    }

    public String getQrCode() {
        return qrCode;
    }
}
