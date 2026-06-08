package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioIdOrderByDataCriacaoDesc(Long destinatarioId);

    long countByDestinatarioIdAndLidaFalse(Long destinatarioId);
}
