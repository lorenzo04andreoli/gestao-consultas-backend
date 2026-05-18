package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistaService {

    private final DentistaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public DentistaService(DentistaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Dentista salvar(Dentista dentista) {
        validarDuplicidadeCadastro(dentista);

        Usuario usuario = validarUsuarioDentista(dentista);

        repository.findByUsuarioId(usuario.getId()).ifPresent(d -> {
            throw new RuntimeException("Usuário já está vinculado a um dentista");
        });

        dentista.setUsuario(usuario);

        return repository.save(dentista);
    }

    public DentistaResponseDto salvarResponse(Dentista dentista) {
        return toResponseDto(salvar(dentista));
    }

    public List<DentistaResponseDto> listar() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Dentista buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
    }

    public DentistaResponseDto buscarResponsePorId(Long id) {
        return toResponseDto(buscarPorId(id));
    }

    public Dentista atualizar(Long id, Dentista dentista) {
        Dentista existente = buscarPorId(id);

        validarDuplicidadeAtualizacao(id, dentista);

        existente.setNome(dentista.getNome());
        existente.setEmail(dentista.getEmail());
        existente.setCpf(dentista.getCpf());
        existente.setCro(dentista.getCro());
        existente.setAtivo(dentista.getAtivo());

        if (dentista.getUsuario() != null) {
            Usuario usuario = validarUsuarioDentista(dentista);

            repository.findByUsuarioId(usuario.getId())
                    .filter(d -> !d.getId().equals(id))
                    .ifPresent(d -> {
                        throw new RuntimeException("Usuário já está vinculado a um dentista");
                    });

            existente.setUsuario(usuario);
        }

        if (dentista.getEspecialidades() != null) {
            existente.setEspecialidades(dentista.getEspecialidades());
        }

        return repository.save(existente);
    }

    public DentistaResponseDto atualizarResponse(Long id, Dentista dentista) {
        return toResponseDto(atualizar(id, dentista));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    private Usuario validarUsuarioDentista(Dentista dentista) {
        if (dentista.getUsuario() == null || dentista.getUsuario().getId() == null) {
            throw new RuntimeException("Usuário vinculado é obrigatório");
        }

        Usuario usuario = usuarioRepository.findById(dentista.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!"DENTISTA".equals(usuario.getPerfil())) {
            throw new RuntimeException("Usuário vinculado deve ter perfil DENTISTA");
        }

        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário vinculado está inativo");
        }

        return usuario;
    }

    private void validarDuplicidadeCadastro(Dentista dentista) {
        repository.findByEmail(dentista.getEmail()).ifPresent(d -> {
            throw new RuntimeException("Email já cadastrado");
        });

        repository.findByCpf(dentista.getCpf()).ifPresent(d -> {
            throw new RuntimeException("CPF já cadastrado");
        });

        repository.findByCro(dentista.getCro()).ifPresent(d -> {
            throw new RuntimeException("CRO já cadastrado");
        });
    }

    private void validarDuplicidadeAtualizacao(Long id, Dentista dentista) {
        repository.findByEmail(dentista.getEmail())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new RuntimeException("Email já cadastrado");
                });

        repository.findByCpf(dentista.getCpf())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new RuntimeException("CPF já cadastrado");
                });

        repository.findByCro(dentista.getCro())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new RuntimeException("CRO já cadastrado");
                });
    }

    private DentistaResponseDto toResponseDto(Dentista d) {
        List<String> especialidades = d.getEspecialidades() == null
                ? List.of()
                : d.getEspecialidades().stream()
                        .map(e -> e.getNome())
                        .toList();

        return new DentistaResponseDto(
                d.getId(),
                d.getNome(),
                d.getCpf(),
                d.getEmail(),
                d.getCro(),
                d.getAtivo(),
                d.getUsuario() == null ? null : d.getUsuario().getId(),
                especialidades
        );
    }
}
