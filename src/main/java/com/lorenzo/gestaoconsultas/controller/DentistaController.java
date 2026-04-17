package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.entity.Dentista;
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
    public Dentista criar(@RequestBody @Valid Dentista dentista) {
        return service.salvar(dentista);
    }

    @GetMapping
    public List<DentistaResponseDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Dentista buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Dentista atualizar(@PathVariable Long id, @RequestBody @Valid Dentista dentista) {
        return service.atualizar(id, dentista);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
