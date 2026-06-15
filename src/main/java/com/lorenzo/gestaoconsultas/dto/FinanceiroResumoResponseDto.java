package com.lorenzo.gestaoconsultas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FinanceiroResumoResponseDto {

    private BigDecimal recebidoMes;
    private BigDecimal aReceber;
    private Long pendentes;
    private Long pagas;
    private Long canceladas;

    public FinanceiroResumoResponseDto(BigDecimal recebidoMes, BigDecimal aReceber,
                                       Long pendentes, Long pagas, Long canceladas) {
        this.recebidoMes = recebidoMes;
        this.aReceber = aReceber;
        this.pendentes = pendentes;
        this.pagas = pagas;
        this.canceladas = canceladas;
    }

    public BigDecimal getRecebidoMes() {
        return recebidoMes;
    }

    @JsonProperty("aReceber")
    public BigDecimal getAReceber() {
        return aReceber;
    }

    public Long getPendentes() {
        return pendentes;
    }

    public Long getPagas() {
        return pagas;
    }

    public Long getCanceladas() {
        return canceladas;
    }
}
