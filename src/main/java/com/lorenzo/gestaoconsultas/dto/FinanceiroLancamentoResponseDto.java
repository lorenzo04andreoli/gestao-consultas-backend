package com.lorenzo.gestaoconsultas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinanceiroLancamentoResponseDto {

    private Long id;
    private Long consultaId;
    private Long pacienteId;
    private String pacienteNome;
    private Long dentistaId;
    private String dentistaNome;
    private String descricao;
    private BigDecimal valor;
    private String tipo;
    private String status;
    private LocalDate dataVencimento;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataCriacao;

    public FinanceiroLancamentoResponseDto(Long id, Long consultaId, Long pacienteId, String pacienteNome,
                                           Long dentistaId, String dentistaNome, String descricao,
                                           BigDecimal valor, String tipo, String status,
                                           LocalDate dataVencimento, LocalDateTime dataPagamento,
                                           LocalDateTime dataCriacao) {
        this.id = id;
        this.consultaId = consultaId;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.dentistaId = dentistaId;
        this.dentistaNome = dentistaNome;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public Long getDentistaId() {
        return dentistaId;
    }

    public String getDentistaNome() {
        return dentistaNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
