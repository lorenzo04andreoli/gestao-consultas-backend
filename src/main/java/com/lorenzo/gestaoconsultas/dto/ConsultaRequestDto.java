package com.lorenzo.gestaoconsultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ConsultaRequestDto {

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long dentistaId;

    @NotNull
    private Long especialidadeId;

    @NotBlank
    private String descricao;

    @NotNull
    private LocalDateTime dataInicio;

    @NotNull
    private LocalDateTime dataFim;

    public Long getPacienteId() { return pacienteId; }
    public Long getDentistaId() { return dentistaId; }
    public Long getEspecialidadeId() { return especialidadeId; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }

}

