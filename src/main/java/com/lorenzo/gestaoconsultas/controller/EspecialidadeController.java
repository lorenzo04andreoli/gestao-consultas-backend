package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.entity.Especialidade;
import com.lorenzo.gestaoconsultas.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {
    private final EspecialidadeService service;

    public EspecialidadeController(EspecialidadeService service) {
        this.service = service;
    }

    @PostMapping
    public Especialidade criar(@RequestBody @Valid Especialidade e) {
        return service.salvar(e);
    }

    @GetMapping
    public List<Especialidade> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Especialidade buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Especialidade atualizar(@PathVariable Long id,
                                   @RequestBody @Valid Especialidade especialidade) {
        return service.atualizar(id, especialidade);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
