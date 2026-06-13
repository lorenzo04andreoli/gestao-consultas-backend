package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.ConsultaRequestDto;
import com.lorenzo.gestaoconsultas.dto.ConsultaResponseDto;
import com.lorenzo.gestaoconsultas.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public ConsultaResponseDto criar(@RequestBody @Valid ConsultaRequestDto dto) {
        return service.agendar(dto);
    }

    @GetMapping
    public List<ConsultaResponseDto> listar() {
        return service.listar();
    }

    @GetMapping("/paginado")
    public Page<ConsultaResponseDto> listarPaginado(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        LocalDateTime inicio = dataInicio != null && !dataInicio.isBlank()
                ? LocalDateTime.parse(dataInicio)
                : null;
        LocalDateTime fim = dataFim != null && !dataFim.isBlank()
                ? LocalDateTime.parse(dataFim)
                : null;

        return service.listarPaginado(termo, status, inicio, fim, pageable);
    }

    @PutMapping("/{id}/cancelar")
    public ConsultaResponseDto cancelar(@PathVariable Long id,
                                        @RequestParam String motivo) {
        return service.cancelar(id, motivo);
    }

    @PutMapping("/{id}/editar")
    public ConsultaResponseDto editar(@PathVariable Long id,
                                     @RequestBody @Valid ConsultaRequestDto dto) {
        return service.editar(id, dto);
    }

    @PutMapping("/{id}/finalizar")
    public ConsultaResponseDto finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }

    @GetMapping("/relatorios")
    public List<ConsultaResponseDto> relatorios(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long dentistaId,
            @RequestParam(required = false) Long especialidadeId,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim
    ) {

        LocalDateTime inicio = dataInicio != null ? LocalDateTime.parse(dataInicio) : null;
        LocalDateTime fim = dataFim != null ? LocalDateTime.parse(dataFim) : null;

        return service.filtrar(pacienteId, dentistaId, especialidadeId, usuarioId, inicio, fim);
    }
}
