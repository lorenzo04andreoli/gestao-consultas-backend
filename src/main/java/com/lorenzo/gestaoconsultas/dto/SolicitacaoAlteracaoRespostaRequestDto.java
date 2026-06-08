package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SolicitacaoAlteracaoRespostaRequestDto {

    @NotBlank
    @Size(max = 2000)
    private String resposta;

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
