package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.SolicitacaoAlteracao;
import com.lorenzo.gestaoconsultas.enums.StatusSolicitacaoAlteracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoAlteracaoRepository extends JpaRepository<SolicitacaoAlteracao, Long> {
    List<SolicitacaoAlteracao> findBySolicitanteIdOrderByDataCriacaoDesc(Long solicitanteId);

    List<SolicitacaoAlteracao> findByStatusOrderByDataCriacaoDesc(StatusSolicitacaoAlteracao status);

    List<SolicitacaoAlteracao> findAllByOrderByDataCriacaoDesc();
}
