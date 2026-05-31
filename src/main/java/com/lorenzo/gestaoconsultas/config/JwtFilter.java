package com.lorenzo.gestaoconsultas.config;

import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import com.lorenzo.gestaoconsultas.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        SecurityContextHolder.clearContext();

        // libera rota de login
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (!jwtService.tokenValido(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String email = jwtService.extrairEmail(token);

            // BUSCA USUÁRIO NO BANCO
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElse(null);

            // VALIDA SE EXISTE E ESTÁ ATIVO
            if (usuario == null || !usuario.getAtivo()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil()))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
