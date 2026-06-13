package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.DentistaAtualizacaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.DentistaCadastroRequestDto;
import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.service.DentistaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/paginado")
    public Page<DentistaResponseDto> listarPaginado(@RequestParam(required = false) String termo,
                                                   @RequestParam(required = false) Boolean ativo,
                                                   @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC)
                                                   Pageable pageable) {
        return service.listarPaginado(termo, ativo, pageable);
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

    @PutMapping("/{id}/desativar")
    public DentistaResponseDto desativar(@PathVariable Long id) {
        return service.desativar(id);
    }

    @PutMapping("/{id}/reativar")
    public DentistaResponseDto reativar(@PathVariable Long id) {
        return service.reativar(id);
    }
}
