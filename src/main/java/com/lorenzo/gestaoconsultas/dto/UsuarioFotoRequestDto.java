package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioFotoRequestDto {

    @NotBlank
    private String fotoPerfil;

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
