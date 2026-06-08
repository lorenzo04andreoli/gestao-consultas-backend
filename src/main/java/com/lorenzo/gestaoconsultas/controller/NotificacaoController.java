package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.NotificacaoResponseDto;
import com.lorenzo.gestaoconsultas.service.NotificacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificacaoResponseDto> listarMinhas() {
        return service.listarMinhas();
    }

    @GetMapping("/nao-lidas/total")
    public Map<String, Long> contarNaoLidas() {
        return Map.of("total", service.contarNaoLidas());
    }

    @PutMapping("/{id}/lida")
    public NotificacaoResponseDto marcarComoLida(@PathVariable Long id) {
        return service.marcarComoLida(id);
    }

    @PutMapping("/lidas")
    public List<NotificacaoResponseDto> marcarTodasComoLidas() {
        return service.marcarTodasComoLidas();
    }
}
