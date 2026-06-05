package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinanceiroPrecoRepository extends JpaRepository<FinanceiroPreco, Long> {

    List<FinanceiroPreco> findByTabelaPrecoIdOrderByEspecialidadeNomeAsc(Long tabelaPrecoId);

    Optional<FinanceiroPreco> findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaId(
            Long especialidadeId,
            Long dentistaId
    );

    Optional<FinanceiroPreco> findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaIsNull(
            Long especialidadeId
    );

    boolean existsByTabelaPrecoIdAndEspecialidadeIdAndDentistaId(
            Long tabelaPrecoId,
            Long especialidadeId,
            Long dentistaId
    );
}
