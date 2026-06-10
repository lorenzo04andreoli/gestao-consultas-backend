package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.LoginRequestDto;
import com.lorenzo.gestaoconsultas.dto.LoginResponseDto;
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
    private final TwoFactorService twoFactorService;

    public AuthService(UsuarioRepository repository,
                       JwtService jwtService,
                       BCryptPasswordEncoder encoder,
                       TwoFactorService twoFactorService) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.twoFactorService = twoFactorService;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais invalidas"));

        if (!usuario.getAtivo()) {
            throw new DisabledException("Usuario inativo");
        }

        if (!encoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais invalidas");
        }

        if (Boolean.TRUE.equals(usuario.getTwoFactorAtivo())) {
            if (usuario.getTwoFactorSecret() == null || usuario.getTwoFactorSecret().isBlank()) {
                usuario.setTwoFactorAtivo(false);
                usuario.setTwoFactorSecret(null);
                usuario.setUltimoLogin(LocalDateTime.now());
                repository.save(usuario);

                return new LoginResponseDto(jwtService.gerarToken(usuario), false);
            }

            if (request.getCodigo2fa() == null || request.getCodigo2fa().isBlank()) {
                return new LoginResponseDto(null, true);
            }

            if (!twoFactorService.codigoValido(usuario.getTwoFactorSecret(), request.getCodigo2fa())) {
                throw new BadCredentialsException("Codigo de autenticacao invalido");
            }
        }

        usuario.setUltimoLogin(LocalDateTime.now());
        repository.save(usuario);

        return new LoginResponseDto(jwtService.gerarToken(usuario), false);
    }
}
