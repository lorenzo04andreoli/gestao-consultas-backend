package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinanceiroPrecoRepository extends JpaRepository<FinanceiroPreco, Long> {

    List<FinanceiroPreco> findByTabelaPrecoIdOrderByEspecialidadeNomeAsc(Long tabelaPrecoId);

    Optional<FinanceiroPreco> findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaIdOrderByTabelaPrecoIdDesc(
            Long especialidadeId,
            Long dentistaId
    );

    Optional<FinanceiroPreco> findFirstByTabelaPrecoAtivoTrueAndAtivoTrueAndEspecialidadeIdAndDentistaIsNullOrderByTabelaPrecoIdDesc(
            Long especialidadeId
    );

    boolean existsByTabelaPrecoIdAndEspecialidadeIdAndDentistaIdAndAtivoTrue(
            Long tabelaPrecoId,
            Long especialidadeId,
            Long dentistaId
    );

    boolean existsByTabelaPrecoIdAndEspecialidadeIdAndDentistaIsNullAndAtivoTrue(
            Long tabelaPrecoId,
            Long especialidadeId
    );
}
