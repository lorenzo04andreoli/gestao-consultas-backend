package com.lorenzo.gestaoconsultas.entity;

import com.lorenzo.gestaoconsultas.enums.StatusSolicitacaoAlteracao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes_alteracao")
public class SolicitacaoAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    @Column(length = 120)
    private String assunto;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacaoAlteracao status = StatusSolicitacaoAlteracao.PENDENTE;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String resposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondida_por_id")
    private Usuario respondidaPor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataResposta;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

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

    public StatusSolicitacaoAlteracao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacaoAlteracao status) {
        this.status = status;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Usuario getRespondidaPor() {
        return respondidaPor;
    }

    public void setRespondidaPor(Usuario respondidaPor) {
        this.respondidaPor = respondidaPor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }
}
