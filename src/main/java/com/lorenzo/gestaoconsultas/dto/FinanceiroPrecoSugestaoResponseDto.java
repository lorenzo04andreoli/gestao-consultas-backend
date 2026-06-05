package com.lorenzo.gestaoconsultas.dto;

import java.math.BigDecimal;

public class FinanceiroPrecoSugestaoResponseDto {

    private Boolean encontrado;
    private Long precoId;
    private BigDecimal valor;
    private String descricao;
    private String origem;

    public FinanceiroPrecoSugestaoResponseDto(Boolean encontrado, Long precoId, BigDecimal valor,
                                              String descricao, String origem) {
        this.encontrado = encontrado;
        this.precoId = precoId;
        this.valor = valor;
        this.descricao = descricao;
        this.origem = origem;
    }

    public Boolean getEncontrado() {
        return encontrado;
    }

    public Long getPrecoId() {
        return precoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getOrigem() {
        return origem;
    }
}
