package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.UsuarioAtualizacaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.UsuarioResponseDto;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public UsuarioResponseDto criar(@RequestBody @Valid Usuario usuario) {
        return service.salvar(usuario);
    }


    @GetMapping
    public List<UsuarioResponseDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(@PathVariable Long id) {
        Usuario u = service.buscarPorId(id);

        return new UsuarioResponseDto(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getPerfil()
        );
    }

    @PutMapping("/{id}")
    public UsuarioResponseDto atualizar(@PathVariable Long id,
                                        @RequestBody @Valid UsuarioAtualizacaoRequestDto dto) {
        return service.atualizar(id, dto);
    }

    @PutMapping("/{id}/desativar")
    public UsuarioResponseDto desativar(@PathVariable Long id) {
        return service.desativar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }




}
