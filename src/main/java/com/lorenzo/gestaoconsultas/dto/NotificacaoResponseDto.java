package com.lorenzo.gestaoconsultas.dto;

import java.time.LocalDateTime;

public class NotificacaoResponseDto {

    private Long id;
    private String titulo;
    private String mensagem;
    private String link;
    private Boolean lida;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataLeitura;

    public NotificacaoResponseDto(Long id,
                                  String titulo,
                                  String mensagem,
                                  String link,
                                  Boolean lida,
                                  LocalDateTime dataCriacao,
                                  LocalDateTime dataLeitura) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.link = link;
        this.lida = lida;
        this.dataCriacao = dataCriacao;
        this.dataLeitura = dataLeitura;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getMensagem() { return mensagem; }
    public String getLink() { return link; }
    public Boolean getLida() { return lida; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataLeitura() { return dataLeitura; }
}
