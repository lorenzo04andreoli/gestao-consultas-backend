package com.lorenzo.gestaoconsultas.dto;

import com.lorenzo.gestaoconsultas.enums.StatusSolicitacaoAlteracao;

import java.time.LocalDateTime;

public class SolicitacaoAlteracaoResponseDto {

    private Long id;
    private Long solicitanteId;
    private String solicitanteNome;
    private String solicitanteEmail;
    private String assunto;
    private String descricao;
    private StatusSolicitacaoAlteracao status;
    private String resposta;
    private Long respondidaPorId;
    private String respondidaPorNome;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataResposta;

    public SolicitacaoAlteracaoResponseDto(Long id,
                                           Long solicitanteId,
                                           String solicitanteNome,
                                           String solicitanteEmail,
                                           String assunto,
                                           String descricao,
                                           StatusSolicitacaoAlteracao status,
                                           String resposta,
                                           Long respondidaPorId,
                                           String respondidaPorNome,
                                           LocalDateTime dataCriacao,
                                           LocalDateTime dataResposta) {
        this.id = id;
        this.solicitanteId = solicitanteId;
        this.solicitanteNome = solicitanteNome;
        this.solicitanteEmail = solicitanteEmail;
        this.assunto = assunto;
        this.descricao = descricao;
        this.status = status;
        this.resposta = resposta;
        this.respondidaPorId = respondidaPorId;
        this.respondidaPorNome = respondidaPorNome;
        this.dataCriacao = dataCriacao;
        this.dataResposta = dataResposta;
    }

    public Long getId() { return id; }
    public Long getSolicitanteId() { return solicitanteId; }
    public String getSolicitanteNome() { return solicitanteNome; }
    public String getSolicitanteEmail() { return solicitanteEmail; }
    public String getAssunto() { return assunto; }
    public String getDescricao() { return descricao; }
    public StatusSolicitacaoAlteracao getStatus() { return status; }
    public String getResposta() { return resposta; }
    public Long getRespondidaPorId() { return respondidaPorId; }
    public String getRespondidaPorNome() { return respondidaPorNome; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataResposta() { return dataResposta; }
}
