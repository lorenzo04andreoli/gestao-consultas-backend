package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SolicitacaoAlteracaoRequestDto {

    @NotBlank
    @Size(max = 120)
    private String assunto;

    @NotBlank
    @Size(max = 2000)
    private String descricao;

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
