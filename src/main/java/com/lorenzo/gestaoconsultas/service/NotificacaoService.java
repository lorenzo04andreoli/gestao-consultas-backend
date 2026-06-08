package com.lorenzo.gestaoconsultas.service;

import com.lorenzo.gestaoconsultas.dto.NotificacaoResponseDto;
import com.lorenzo.gestaoconsultas.entity.Notificacao;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import com.lorenzo.gestaoconsultas.repository.NotificacaoRepository;
import com.lorenzo.gestaoconsultas.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              UsuarioRepository usuarioRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Notificacao criar(Usuario destinatario, String titulo, String mensagem, String link) {
        Notificacao notificacao = new Notificacao();
        notificacao.setDestinatario(destinatario);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setLink(link);
        return notificacaoRepository.save(notificacao);
    }

    public List<NotificacaoResponseDto> listarMinhas() {
        Usuario usuario = usuarioAutenticado();

        return notificacaoRepository.findByDestinatarioIdOrderByDataCriacaoDesc(usuario.getId()).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public long contarNaoLidas() {
        Usuario usuario = usuarioAutenticado();
        return notificacaoRepository.countByDestinatarioIdAndLidaFalse(usuario.getId());
    }

    public NotificacaoResponseDto marcarComoLida(Long id) {
        Usuario usuario = usuarioAutenticado();
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacao nao encontrada"));

        if (!notificacao.getDestinatario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado a notificacao");
        }

        notificacao.setLida(true);
        notificacao.setDataLeitura(LocalDateTime.now());
        return toResponseDto(notificacaoRepository.save(notificacao));
    }

    public List<NotificacaoResponseDto> marcarTodasComoLidas() {
        Usuario usuario = usuarioAutenticado();
        List<Notificacao> notificacoes = notificacaoRepository.findByDestinatarioIdOrderByDataCriacaoDesc(usuario.getId());
        LocalDateTime agora = LocalDateTime.now();

        notificacoes.stream()
                .filter(notificacao -> !Boolean.TRUE.equals(notificacao.getLida()))
                .forEach(notificacao -> {
                    notificacao.setLida(true);
                    notificacao.setDataLeitura(agora);
                });

        return notificacaoRepository.saveAll(notificacoes).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private Usuario usuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado nao encontrado"));
    }

    private NotificacaoResponseDto toResponseDto(Notificacao notificacao) {
        return new NotificacaoResponseDto(
                notificacao.getId(),
                notificacao.getTitulo(),
                notificacao.getMensagem(),
                notificacao.getLink(),
                notificacao.getLida(),
                notificacao.getDataCriacao(),
                notificacao.getDataLeitura()
        );
    }
}
