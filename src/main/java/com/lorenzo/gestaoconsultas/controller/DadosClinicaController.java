package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.DadosClinicaRequestDto;
import com.lorenzo.gestaoconsultas.dto.DadosClinicaResponseDto;
import com.lorenzo.gestaoconsultas.service.DadosClinicaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinica")
public class DadosClinicaController {

    private final DadosClinicaService service;

    public DadosClinicaController(DadosClinicaService service) {
        this.service = service;
    }

    @GetMapping
    public DadosClinicaResponseDto buscar() {
        return service.buscar();
    }

    @PutMapping
    public DadosClinicaResponseDto atualizar(@RequestBody @Valid DadosClinicaRequestDto dto) {
        return service.atualizar(dto);
    }
}
