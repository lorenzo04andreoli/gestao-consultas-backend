package com.lorenzo.gestaoconsultas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultaResponseDto {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private Long dentistaId;
    private String dentistaNome;
    private Long especialidadeId;
    private String especialidadeNome;
    private String usuarioNome;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private BigDecimal valor;
    private String status;
    private String motivoCancelamento;

    public ConsultaResponseDto(Long id, Long pacienteId, String pacienteNome, String pacienteTelefone,
                               Long dentistaId, String dentistaNome, Long especialidadeId,
                               String especialidadeNome, String usuarioNome, String descricao,
                               LocalDateTime dataInicio, LocalDateTime dataFim, BigDecimal valor,
                               String status, String motivoCancelamento) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.pacienteTelefone = pacienteTelefone;
        this.dentistaId = dentistaId;
        this.dentistaNome = dentistaNome;
        this.especialidadeId = especialidadeId;
        this.especialidadeNome = especialidadeNome;
        this.usuarioNome = usuarioNome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
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

    public String getPacienteTelefone() {
        return pacienteTelefone;
    }

    public Long getDentistaId() {
        return dentistaId;
    }

    public String getDentistaNome() {
        return dentistaNome;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public String getEspecialidadeNome() {
        return especialidadeNome;
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

    public BigDecimal getValor() {
        return valor;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
}
