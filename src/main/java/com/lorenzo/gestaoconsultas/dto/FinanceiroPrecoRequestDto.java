package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class FinanceiroPrecoRequestDto {

    @NotNull
    private Long tabelaPrecoId;

    @NotNull
    private Long especialidadeId;

    private Long dentistaId;

    @NotBlank
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;

    private Boolean ativo = true;

    public Long getTabelaPrecoId() {
        return tabelaPrecoId;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public Long getDentistaId() {
        return dentistaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
