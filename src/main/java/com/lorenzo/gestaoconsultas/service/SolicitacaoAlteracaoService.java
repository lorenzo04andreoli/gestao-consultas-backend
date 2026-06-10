package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoRequestDto;
import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoResponseDto;
import com.lorenzo.gestaoconsultas.dto.SolicitacaoAlteracaoRespostaRequestDto;
import com.lorenzo.gestaoconsultas.entity.SolicitacaoAlteracao;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.enums.StatusSolicitacaoAlteracao;
import com.lorenzo.gestaoconsultas.repository.SolicitacaoAlteracaoRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoAlteracaoService {

    private final SolicitacaoAlteracaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public SolicitacaoAlteracaoService(SolicitacaoAlteracaoRepository solicitacaoRepository,
                                       UsuarioRepository usuarioRepository,
                                       NotificacaoService notificacaoService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
    }

    public SolicitacaoAlteracaoResponseDto criar(SolicitacaoAlteracaoRequestDto dto) {
        Usuario solicitante = usuarioAutenticado();

        SolicitacaoAlteracao solicitacao = new SolicitacaoAlteracao();
        solicitacao.setSolicitante(solicitante);
        solicitacao.setAssunto(dto.getAssunto().trim());
        solicitacao.setDescricao(dto.getDescricao().trim());

        SolicitacaoAlteracao salva = solicitacaoRepository.save(solicitacao);
        notificarAdmins(salva);

        return toResponseDto(salva);
    }

    public List<SolicitacaoAlteracaoResponseDto> listarMinhas() {
        Usuario usuario = usuarioAutenticado();

        return solicitacaoRepository.findBySolicitanteIdOrderByDataCriacaoDesc(usuario.getId()).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<SolicitacaoAlteracaoResponseDto> listarAdmin() {
        return solicitacaoRepository.findAllByOrderByDataCriacaoDesc().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<SolicitacaoAlteracaoResponseDto> listarPendentes() {
        return solicitacaoRepository.findByStatusOrderByDataCriacaoDesc(StatusSolicitacaoAlteracao.PENDENTE).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public SolicitacaoAlteracaoResponseDto responder(Long id, SolicitacaoAlteracaoRespostaRequestDto dto) {
        Usuario admin = usuarioAutenticado();
        SolicitacaoAlteracao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitacao nao encontrada"));

        if (solicitacao.getStatus() == StatusSolicitacaoAlteracao.RESPONDIDA) {
            throw new RuntimeException("Solicitacao ja respondida");
        }

        solicitacao.setResposta(dto.getResposta().trim());
        solicitacao.setRespondidaPor(admin);
        solicitacao.setStatus(StatusSolicitacaoAlteracao.RESPONDIDA);
        solicitacao.setDataResposta(LocalDateTime.now());

        SolicitacaoAlteracao salva = solicitacaoRepository.save(solicitacao);
        notificarSolicitante(salva);

        return toResponseDto(salva);
    }

    private void notificarAdmins(SolicitacaoAlteracao solicitacao) {
        List<Usuario> admins = usuarioRepository.findByPerfilAndAtivoTrue("ADMIN");

        admins.forEach(admin -> notificacaoService.criar(
                admin,
                "Nova solicitacao de alteracao",
                solicitacao.getSolicitante().getNome() + " solicitou alteracao: " + solicitacao.getAssunto() + ".",
                "/solicitacoes-alteracao"
        ));
    }

    private void notificarSolicitante(SolicitacaoAlteracao solicitacao) {
        notificacaoService.criar(
                solicitacao.getSolicitante(),
                "Solicitacao respondida",
                "O admin respondeu sua solicitacao de alteracao.",
                "/perfil"
        );
    }

    private Usuario usuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado nao encontrado"));
    }

    private SolicitacaoAlteracaoResponseDto toResponseDto(SolicitacaoAlteracao solicitacao) {
        Usuario respondidaPor = solicitacao.getRespondidaPor();

        return new SolicitacaoAlteracaoResponseDto(
                solicitacao.getId(),
                solicitacao.getSolicitante().getId(),
                solicitacao.getSolicitante().getNome(),
                solicitacao.getSolicitante().getEmail(),
                solicitacao.getAssunto(),
                solicitacao.getDescricao(),
                solicitacao.getStatus(),
                solicitacao.getResposta(),
                respondidaPor == null ? null : respondidaPor.getId(),
                respondidaPor == null ? null : respondidaPor.getNome(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataResposta()
        );
    }
}
