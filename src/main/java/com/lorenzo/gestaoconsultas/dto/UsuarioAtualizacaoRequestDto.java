package com.lorenzo.gestaoconsultas.dto;

import com.lorenzo.gestaoconsultas.validation.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioAtualizacaoRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    @CPF
    private String cpf;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String perfil;

    private Boolean ativo = true;

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
