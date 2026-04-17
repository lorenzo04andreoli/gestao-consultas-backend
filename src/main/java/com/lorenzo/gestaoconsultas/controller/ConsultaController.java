package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public Consulta criar(@RequestBody @Valid Consulta consulta) {
        return service.agendar(consulta);
    }

    @GetMapping
    public List<Consulta> listar(
            @RequestParam (required = false) Long dentistaId,
            @RequestParam (required = false) String perfil,
            @RequestParam (required = false) Long usuarioId

    ){
        return service.listar(perfil, dentistaId, usuarioId);
    }

    @PutMapping("/{id}/cancelar")
    public Consulta cancelar(@PathVariable Long id,
                             @RequestParam String motivo) {
        return service.cancelar(id, motivo);
    }
}
