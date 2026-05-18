package com.lorenzo.gestaoconsultas.service;


import com.lorenzo.gestaoconsultas.dto.LoginRequestDto;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    public AuthService (UsuarioRepository repository, JwtService jwtService, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    public String login(LoginRequestDto request) {
        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!usuario.getAtivo()) {
            throw new DisabledException("Usuário inativo");
        }

        if (!encoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        usuario.setUltimoLogin(LocalDateTime.now());
        repository.save(usuario);

        return jwtService.gerarToken(usuario);
    }

}
