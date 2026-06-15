package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinanceiroLancamentoRequestDto {

    @NotNull
    private Long consultaId;

    @NotBlank
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;

    private LocalDate dataVencimento;

    public Long getConsultaId() {
        return consultaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
}
