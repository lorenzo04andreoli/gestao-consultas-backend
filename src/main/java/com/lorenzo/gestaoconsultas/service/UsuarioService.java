package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.lorenzo.gestaoconsultas.dto.UsuarioResponseDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
        this.repository = repository;
    }


    public UsuarioResponseDto salvar(Usuario usuario){
        repository.findByEmail(usuario.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email ja cadastrado");
        });

        repository.findByCpf(usuario.getCpf()).ifPresent(u -> {
            throw new RuntimeException("CPF ja cadastrado");
        });

        usuario.setSenha(encoder.encode(usuario.getSenha()));
        Usuario salvo = repository.save(usuario);
        return new UsuarioResponseDto(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getPerfil()
        );
    }

    public List<UsuarioResponseDto> listar(){
        return repository.findAll().stream()
                .map(u -> new UsuarioResponseDto(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getPerfil()
                ))
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
