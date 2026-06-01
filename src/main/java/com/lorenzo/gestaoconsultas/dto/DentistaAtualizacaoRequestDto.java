package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DentistaAtualizacaoRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String cro;

    private Boolean ativo = true;

    @NotEmpty
    private List<Long> especialidadeIds;

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getCro() {
        return cro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public List<Long> getEspecialidadeIds() {
        return especialidadeIds;
    }
}
