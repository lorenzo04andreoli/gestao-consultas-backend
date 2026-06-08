package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TwoFactorConfirmRequestDto {

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Codigo deve ter 6 digitos")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
