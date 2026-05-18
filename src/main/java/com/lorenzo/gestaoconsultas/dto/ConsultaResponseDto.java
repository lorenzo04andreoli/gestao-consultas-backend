package com.lorenzo.gestaoconsultas.dto;

import java.time.LocalDateTime;

public class ConsultaResponseDto {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long dentistaId;
    private String dentistaNome;
    private String usuarioNome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String status;
    private String motivoCancelamento;

    public ConsultaResponseDto(Long id, Long pacienteId, String pacienteNome, Long dentistaId,
                               String dentistaNome, String usuarioNome, String descricao,
                               LocalDateTime dataInicio, LocalDateTime dataFim, String status,
                               String motivoCancelamento) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.dentistaId = dentistaId;
        this.dentistaNome = dentistaNome;
        this.usuarioNome = usuarioNome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.motivoCancelamento = motivoCancelamento;
    }

    public Long getId() {
        return id;
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

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
}
