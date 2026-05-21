package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.ConsultaRequestDto;
import com.lorenzo.gestaoconsultas.dto.ConsultaResponseDto;
import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.entity.Paciente;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.enums.StatusConsulta;
import com.lorenzo.gestaoconsultas.repository.ConsultaRepository;
import com.lorenzo.gestaoconsultas.repository.DentistaRepository;
import com.lorenzo.gestaoconsultas.repository.PacienteRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;

    public ConsultaService(ConsultaRepository repository, UsuarioRepository usuarioRepository,
                           PacienteRepository pacienteRepository, DentistaRepository dentistaRepository) {
        this.dentistaRepository = dentistaRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.repository = repository;
    }

    public ConsultaResponseDto agendar(ConsultaRequestDto dto) {
        Usuario usuario = getUsuarioAutenticado();

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(dto.getDentistaId())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

        if (isDentista(usuario) && !dentista.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Dentista não pode agendar consultas para outro dentista");
        }

        Consulta consulta = new Consulta();
        consulta.setUsuario(usuario);
        consulta.setPaciente(paciente);
        consulta.setDentista(dentista);
        consulta.setDescricao(dto.getDescricao());
        consulta.setDataInicio(dto.getDataInicio());
        consulta.setDataFim(dto.getDataFim());

        validarAgendamento(usuario, dentista, consulta);

        return toResponseDto(repository.save(consulta));
    }

    public ConsultaResponseDto finalizar(Long id) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        validarAcessoDentista(consulta);

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RuntimeException("Consulta cancelada não pode ser finalizada");
        }

        if (consulta.getStatus() == StatusConsulta.FINALIZADA) {
            throw new RuntimeException("Consulta já finalizada");
        }

        if (consulta.getDataInicio().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Consulta ainda não começou");
        }

        consulta.setStatus(StatusConsulta.FINALIZADA);

        return toResponseDto(repository.save(consulta));
    }

    public List<ConsultaResponseDto> listar() {
        Usuario usuario = getUsuarioAutenticado();

        if (isDentista(usuario)) {
            return toResponseDtoList(repository.findByDentistaUsuarioId(usuario.getId()));
        }

        return toResponseDtoList(repository.findAll());
    }

    public ConsultaResponseDto cancelar(Long id, String motivo) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        validarAcessoDentista(consulta);

        if (motivo == null || motivo.trim().isEmpty()) {
            throw new RuntimeException("Motivo é obrigatório");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consulta.setMotivoCancelamento(motivo.trim());

        return toResponseDto(repository.save(consulta));
    }

    public List<ConsultaResponseDto> filtrar(Long pacienteId, Long dentistaId, Long especialidadeId,
                                             Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        Usuario usuario = getUsuarioAutenticado();
        Long usuarioDentistaId = isDentista(usuario) ? usuario.getId() : null;

        return toResponseDtoList(repository.filtrarConsultas(
                usuarioDentistaId,
                pacienteId,
                dentistaId,
                especialidadeId,
                usuarioId,
                inicio,
                fim
        ));
    }

    private void validarAgendamento(Usuario usuario, Dentista dentista, Consulta consulta) {
        if (!dentista.getAtivo()) {
            throw new RuntimeException("Dentista está inativo");
        }

        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuário está inativo");
        }

        if (consulta.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data de início não pode ser no passado");
        }

        if (!consulta.getDataFim().isAfter(consulta.getDataInicio())) {
            throw new RuntimeException("Data de fim deve ser após a data de início");
        }

        boolean conflito = repository.existeConflito(
                dentista,
                consulta.getDataInicio(),
                consulta.getDataFim(),
                StatusConsulta.CANCELADA
        );

        if (conflito) {
            throw new RuntimeException("Dentista já possui uma consulta nesse horário");
        }
    }

    private Usuario getUsuarioAutenticado() {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private boolean isDentista(Usuario usuario) {
        return "DENTISTA".equals(usuario.getPerfil());
    }

    private void validarAcessoDentista(Consulta consulta) {
        Usuario usuario = getUsuarioAutenticado();

        if (isDentista(usuario) && !consulta.getDentista().getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Dentista não tem permissão para acessar esta consulta");
        }
    }

    private List<ConsultaResponseDto> toResponseDtoList(List<Consulta> consultas) {
        return consultas.stream()
                .map(this::toResponseDto)
                .toList();
    }

    private ConsultaResponseDto toResponseDto(Consulta consulta) {
        return new ConsultaResponseDto(
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNome(),
                consulta.getDentista().getId(),
                consulta.getDentista().getNome(),
                consulta.getUsuario().getNome(),
                consulta.getDescricao(),
                consulta.getDataInicio(),
                consulta.getDataFim(),
                consulta.getStatus().name(),
                consulta.getMotivoCancelamento()
        );
    }

    public ConsultaResponseDto editar(Long id, ConsultaRequestDto dto) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        validarAcessoDentista(consulta);

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RuntimeException("Consulta cancelada não pode ser editada");
        }

        if (consulta.getStatus() == StatusConsulta.FINALIZADA) {
            throw new RuntimeException("Consulta finalizada não pode ser editada");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(dto.getDentistaId())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
        Usuario usuario = getUsuarioAutenticado();

        if (isDentista(usuario) && !dentista.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Dentista nao pode transferir consulta para outro dentista");
        }

        consulta.setPaciente(paciente);
        consulta.setDentista(dentista);
        consulta.setDescricao(dto.getDescricao());
        consulta.setDataInicio(dto.getDataInicio());
        consulta.setDataFim(dto.getDataFim());

        validarEdicao(usuario, dentista, consulta);

        return toResponseDto(repository.save(consulta));
    }

    private void validarEdicao(Usuario usuario, Dentista dentista, Consulta consulta) {
        if (!dentista.getAtivo()) {
            throw new RuntimeException("Dentista esta inativo");
        }

        if (!usuario.getAtivo()) {
            throw new RuntimeException("Usuario esta inativo");
        }

        if (consulta.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Data de inicio nao pode ser no passado");
        }

        if (!consulta.getDataFim().isAfter(consulta.getDataInicio())) {
            throw new RuntimeException("Data de fim deve ser apos a data de inicio");
        }

        boolean conflito = repository.existeConflitoAoEditar(
                consulta.getId(),
                dentista,
                consulta.getDataInicio(),
                consulta.getDataFim(),
                StatusConsulta.CANCELADA
        );

        if (conflito) {
            throw new RuntimeException("Dentista ja possui uma consulta nesse horario");
        }
    }
}
