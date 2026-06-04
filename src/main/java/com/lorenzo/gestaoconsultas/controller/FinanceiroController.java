package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoRequestDto;
import com.lorenzo.gestaoconsultas.dto.FinanceiroLancamentoResponseDto;
import com.lorenzo.gestaoconsultas.service.FinanceiroService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/financeiro")
public class FinanceiroController {

    private final FinanceiroService service;

    public FinanceiroController(FinanceiroService service) {
        this.service = service;
    }

    @GetMapping("/lancamentos")
    public List<FinanceiroLancamentoResponseDto> listarLancamentos() {
        return service.listarLancamentos();
    }

    @PostMapping("/lancamentos")
    public FinanceiroLancamentoResponseDto criarLancamento(
            @RequestBody @Valid FinanceiroLancamentoRequestDto dto) {
        return service.criarCobranca(dto);
    }
}
