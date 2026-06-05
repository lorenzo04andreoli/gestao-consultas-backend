package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;

public class FinanceiroTabelaPrecoRequestDto {

    @NotBlank
    private String nome;

    private Boolean ativo = true;

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
