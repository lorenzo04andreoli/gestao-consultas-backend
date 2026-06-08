package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.TwoFactorConfirmRequestDto;
import com.lorenzo.gestaoconsultas.dto.TwoFactorSetupResponseDto;
import com.lorenzo.gestaoconsultas.dto.UsuarioAtualizacaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.UsuarioFotoRequestDto;
import com.lorenzo.gestaoconsultas.dto.UsuarioResponseDto;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/me")
    public UsuarioResponseDto perfilAutenticado() {
        return service.buscarPerfilAutenticado();
    }

    @PutMapping("/me/foto")
    public UsuarioResponseDto atualizarFotoPerfil(@RequestBody @Valid UsuarioFotoRequestDto dto) {
        return service.atualizarFotoPerfil(dto.getFotoPerfil());
    }

    @DeleteMapping("/me/foto")
    public UsuarioResponseDto removerFotoPerfil() {
        return service.removerFotoPerfil();
    }

    @PostMapping("/me/2fa/setup")
    public TwoFactorSetupResponseDto iniciarTwoFactor() {
        return service.iniciarTwoFactor();
    }

    @PostMapping("/me/2fa/confirm")
    public UsuarioResponseDto confirmarTwoFactor(@RequestBody @Valid TwoFactorConfirmRequestDto dto) {
        return service.confirmarTwoFactor(dto.getCodigo());
    }

    @DeleteMapping("/me/2fa")
    public UsuarioResponseDto desativarTwoFactor() {
        return service.desativarTwoFactor();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(@PathVariable Long id) {
        Usuario u = service.buscarPorId(id);

        return new UsuarioResponseDto(
                u.getId(),
                u.getNome(),
                u.getCpf(),
                u.getEmail(),
                u.getPerfil(),
                u.getAtivo(),
                u.getFotoPerfil(),
                u.getTwoFactorAtivo()
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
