package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.entity.Especialidade;
import com.lorenzo.gestaoconsultas.service.EspecialidadeService;
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
    public Especialidade criar(@RequestBody Especialidade e) {
        return service.salvar(e);
    }

    @GetMapping
    public List<Especialidade> listar() {
        return service.listar();
    }

}
