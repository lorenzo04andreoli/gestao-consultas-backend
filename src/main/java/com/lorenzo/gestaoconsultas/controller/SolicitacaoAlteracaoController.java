package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoResponseDto;
import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoRespostaRequestDto;
import com.lorenzo.gestaoconsultas.service.SolicitacaoAlteracaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/solicitacoes-alteracao")
public class SolicitacaoAlteracaoController {

    private final SolicitacaoAlteracaoService service;

    public SolicitacaoAlteracaoController(SolicitacaoAlteracaoService service) {
        this.service = service;
    }

    @PostMapping
    public SolicitacaoAlteracaoResponseDto criar(@RequestBody @Valid SolicitacaoAlteracaoRequestDto dto) {
        return service.criar(dto);
    }

    @GetMapping("/minhas")
    public List<SolicitacaoAlteracaoResponseDto> listarMinhas() {
        return service.listarMinhas();
    }

    @GetMapping("/admin")
    public List<SolicitacaoAlteracaoResponseDto> listarAdmin() {
        return service.listarAdmin();
    }

    @GetMapping("/admin/pendentes")
    public List<SolicitacaoAlteracaoResponseDto> listarPendentes() {
        return service.listarPendentes();
    }

    @PostMapping("/{id}/responder")
    public SolicitacaoAlteracaoResponseDto responder(@PathVariable Long id,
                                                     @RequestBody @Valid SolicitacaoAlteracaoRespostaRequestDto dto) {
        return service.responder(id, dto);
    }
}
