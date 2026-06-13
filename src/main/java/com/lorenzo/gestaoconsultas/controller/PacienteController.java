package com.lorenzo.gestaoconsultas.controller;


import com.lorenzo.gestaoconsultas.entity.Paciente;
import com.lorenzo.gestaoconsultas.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping
    public Paciente criar(@RequestBody @Valid Paciente paciente) {
        return service.salvar(paciente);
    }

    @GetMapping
    public List<Paciente> listar() {
        return service.listar();
    }

    @GetMapping("/paginado")
    public Page<Paciente> listarPaginado(@RequestParam(required = false) String termo,
                                         @RequestParam(required = false) Boolean ativo,
                                         @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC)
                                         Pageable pageable) {
        return service.listarPaginado(termo, ativo, pageable);
    }

    @GetMapping("/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Paciente atualizar(@PathVariable Long id,
                              @RequestBody @Valid Paciente paciente) {
        return service.atualizar(id, paciente);
    }

    @PutMapping("/{id}/desativar")
    public Paciente desativar(@PathVariable Long id) {
        return service.desativar(id);
    }

    @PutMapping("/{id}/reativar")
    public Paciente reativar(@PathVariable Long id) {
        return service.reativar(id);
    }

    @DeleteMapping("/{id}")
    public Paciente deletar(@PathVariable Long id) {
        return service.deletar(id);
    }

}
