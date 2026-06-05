package com.lorenzo.gestaoconsultas.dto;

import java.time.LocalDateTime;

public class FinanceiroTabelaPrecoResponseDto {

    private Long id;
    private String nome;
    private Boolean ativo;
    private LocalDateTime dataCriacao;

    public FinanceiroTabelaPrecoResponseDto(Long id, String nome, Boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
