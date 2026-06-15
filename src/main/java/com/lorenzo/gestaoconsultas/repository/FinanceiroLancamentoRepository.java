package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import com.lorenzo.gestaoconsultas.enums.StatusLancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FinanceiroLancamentoRepository extends JpaRepository<FinanceiroLancamento, Long> {

    boolean existsByConsultaId(Long consultaId);

    Optional<FinanceiroLancamento> findByConsultaId(Long consultaId);

    List<FinanceiroLancamento> findAllByOrderByDataCriacaoDesc();

    List<FinanceiroLancamento> findByStatus(StatusLancamentoFinanceiro status);

    Long countByStatus(StatusLancamentoFinanceiro status);

    @Query("""
    SELECT COALESCE(SUM(l.valor), 0) FROM FinanceiroLancamento l
    WHERE l.status = :status
    """)
    BigDecimal somarPorStatus(@Param("status") StatusLancamentoFinanceiro status);

    @Query("""
    SELECT COALESCE(SUM(l.valor), 0) FROM FinanceiroLancamento l
    WHERE l.status = :status
    AND l.dataPagamento >= :inicio
    AND l.dataPagamento < :fim
    """)
    BigDecimal somarPorStatusEDataPagamento(
            @Param("status") StatusLancamentoFinanceiro status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
