package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.UsuarioAtualizacaoRequestDto;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.lorenzo.gestaoconsultas.dto.UsuarioResponseDto;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final DentistaRepository dentistaRepository;

    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository,
                          DentistaRepository dentistaRepository,
                          BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
        this.repository = repository;
        this.dentistaRepository = dentistaRepository;
    }


    public UsuarioResponseDto salvar(Usuario usuario){
        if ("DENTISTA".equals(usuario.getPerfil())) {
            throw new RuntimeException("Usuario dentista deve ser cadastrado pela tela de dentistas");
        }

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
                salvo.getCpf(),
                salvo.getEmail(),
                salvo.getPerfil(),
                salvo.getAtivo(),
                salvo.getFotoPerfil()
        );
    }

    public List<UsuarioResponseDto> listar(){
        return repository.findAll().stream()
                .map(u -> new UsuarioResponseDto(
                        u.getId(),
                        u.getNome(),
                        u.getCpf(),
                        u.getEmail(),
                        u.getPerfil(),
                        u.getAtivo(),
                        u.getFotoPerfil()
                ))
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDto atualizar(Long id, UsuarioAtualizacaoRequestDto dto) {
        Usuario usuario = buscarPorId(id);

        dentistaRepository.findByUsuarioId(id).ifPresent(d -> {
            throw new RuntimeException("Usuario vinculado a dentista deve ser editado pela tela de dentistas");
        });

        if ("DENTISTA".equals(dto.getPerfil())) {
            throw new RuntimeException("Perfil DENTISTA deve ser gerenciado pela tela de dentistas");
        }

        repository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new RuntimeException("Email ja cadastrado");
                });

        repository.findByCpf(dto.getCpf())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new RuntimeException("CPF ja cadastrado");
                });

        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(dto.getAtivo() == null || dto.getAtivo());

        return toResponseDto(repository.save(usuario));
    }

    public UsuarioResponseDto desativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        return toResponseDto(repository.save(usuario));
    }

    public UsuarioResponseDto buscarPerfilAutenticado() {
        return toResponseDto(buscarUsuarioAutenticado());
    }

    public UsuarioResponseDto atualizarFotoPerfil(String fotoPerfil) {
        validarFotoPerfil(fotoPerfil);

        Usuario usuario = buscarUsuarioAutenticado();
        usuario.setFotoPerfil(fotoPerfil);
        return toResponseDto(repository.save(usuario));
    }

    public UsuarioResponseDto removerFotoPerfil() {
        Usuario usuario = buscarUsuarioAutenticado();
        usuario.setFotoPerfil(null);
        return toResponseDto(repository.save(usuario));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    private Usuario buscarUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado nao encontrado"));
    }

    private void validarFotoPerfil(String fotoPerfil) {
        if (fotoPerfil == null || !fotoPerfil.startsWith("data:image/")) {
            throw new RuntimeException("Foto de perfil invalida");
        }

        if (fotoPerfil.length() > 2_000_000) {
            throw new RuntimeException("Foto de perfil deve ter no maximo 2 MB");
        }
    }

    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getFotoPerfil()
        );
    }
}
