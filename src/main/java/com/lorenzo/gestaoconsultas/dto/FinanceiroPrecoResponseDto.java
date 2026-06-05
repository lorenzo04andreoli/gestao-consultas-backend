package com.lorenzo.gestaoconsultas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinanceiroPrecoResponseDto {

    private Long id;
    private Long tabelaPrecoId;
    private String tabelaPrecoNome;
    private Long especialidadeId;
    private String especialidadeNome;
    private Long dentistaId;
    private String dentistaNome;
    private String descricao;
    private BigDecimal valor;
    private Boolean ativo;
    private LocalDateTime dataCriacao;

    public FinanceiroPrecoResponseDto(Long id, Long tabelaPrecoId, String tabelaPrecoNome,
                                      Long especialidadeId, String especialidadeNome,
                                      Long dentistaId, String dentistaNome, String descricao,
                                      BigDecimal valor, Boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.tabelaPrecoId = tabelaPrecoId;
        this.tabelaPrecoNome = tabelaPrecoNome;
        this.especialidadeId = especialidadeId;
        this.especialidadeNome = especialidadeNome;
        this.dentistaId = dentistaId;
        this.dentistaNome = dentistaNome;
        this.descricao = descricao;
        this.valor = valor;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public Long getTabelaPrecoId() {
        return tabelaPrecoId;
    }

    public String getTabelaPrecoNome() {
        return tabelaPrecoNome;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public String getEspecialidadeNome() {
        return especialidadeNome;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
