package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.DentistaCadastroRequestDto;
import com.lorenzo.gestaoconsultas.dto.DentistaResponseDto;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.entity.Especialidade;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import com.lorenzo.gestaoconsultas.repository.EspecialidadeRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DentistaService {

    private final DentistaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final BCryptPasswordEncoder encoder;

    public DentistaService(DentistaRepository repository,
                           UsuarioRepository usuarioRepository,
                           EspecialidadeRepository especialidadeRepository,
                           BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.encoder = encoder;
    }

    @Transactional
    public DentistaResponseDto cadastrarComUsuario(DentistaCadastroRequestDto dto) {
        validarDuplicidadeUsuario(dto);
        validarDuplicidadeDentista(dto);

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(encoder.encode(dto.getSenha()));
        usuario.setPerfil("DENTISTA");
        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Dentista dentista = new Dentista();
        dentista.setNome(dto.getNome());
        dentista.setCpf(dto.getCpf());
        dentista.setEmail(dto.getEmail());
        dentista.setCro(dto.getCro());
        dentista.setAtivo(dto.getAtivo() == null || dto.getAtivo());
        dentista.setUsuario(usuarioSalvo);
        dentista.setEspecialidades(buscarEspecialidades(dto.getEspecialidadeIds()));

        return toResponseDto(repository.save(dentista));
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

        sincronizarUsuarioVinculado(existente);

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

    private void validarDuplicidadeUsuario(DentistaCadastroRequestDto dto) {
        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email ja cadastrado");
        });

        usuarioRepository.findByCpf(dto.getCpf()).ifPresent(u -> {
            throw new RuntimeException("CPF ja cadastrado");
        });
    }

    private void validarDuplicidadeDentista(DentistaCadastroRequestDto dto) {
        repository.findByEmail(dto.getEmail()).ifPresent(d -> {
            throw new RuntimeException("Email ja cadastrado");
        });

        repository.findByCpf(dto.getCpf()).ifPresent(d -> {
            throw new RuntimeException("CPF ja cadastrado");
        });

        repository.findByCro(dto.getCro()).ifPresent(d -> {
            throw new RuntimeException("CRO ja cadastrado");
        });
    }

    private List<Especialidade> buscarEspecialidades(List<Long> especialidadeIds) {
        List<Especialidade> especialidades = especialidadeRepository.findAllById(especialidadeIds);
        Set<Long> idsEncontrados = especialidades.stream()
                .map(Especialidade::getId)
                .collect(Collectors.toSet());

        List<Long> idsNaoEncontrados = especialidadeIds.stream()
                .filter(id -> !idsEncontrados.contains(id))
                .toList();

        if (!idsNaoEncontrados.isEmpty()) {
            throw new RuntimeException("Especialidade nao encontrada: " + idsNaoEncontrados);
        }

        return especialidades;
    }

    private void sincronizarUsuarioVinculado(Dentista dentista) {
        Usuario usuario = dentista.getUsuario();

        validarDuplicidadeUsuarioAtualizacao(usuario.getId(), dentista);

        usuario.setNome(dentista.getNome());
        usuario.setCpf(dentista.getCpf());
        usuario.setEmail(dentista.getEmail());

        usuarioRepository.save(usuario);
    }

    private void validarDuplicidadeUsuarioAtualizacao(Long usuarioId, Dentista dentista) {
        usuarioRepository.findByEmail(dentista.getEmail())
                .filter(u -> !u.getId().equals(usuarioId))
                .ifPresent(u -> {
                    throw new RuntimeException("Email ja cadastrado");
                });

        usuarioRepository.findByCpf(dentista.getCpf())
                .filter(u -> !u.getId().equals(usuarioId))
                .ifPresent(u -> {
                    throw new RuntimeException("CPF ja cadastrado");
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
