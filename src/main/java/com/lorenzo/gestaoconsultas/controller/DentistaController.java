package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.DentistaAtualizacaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.DentistaCadastroRequestDto;
import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.service.DentistaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentistas")
public class DentistaController {

    private final DentistaService service;


    public DentistaController(DentistaService service) {
        this.service = service;
    }

    @PostMapping
    public DentistaResponseDto criar(@RequestBody @Valid DentistaCadastroRequestDto dto) {
        return service.cadastrarComUsuario(dto);
    }

    @GetMapping
    public List<DentistaResponseDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public DentistaResponseDto buscarPorId(@PathVariable Long id) {
        return service.buscarResponsePorId(id);
    }

    @PutMapping("/{id}")
    public DentistaResponseDto atualizar(@PathVariable Long id,
                                         @RequestBody @Valid DentistaAtualizacaoRequestDto dto) {
        return service.atualizarResponse(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
