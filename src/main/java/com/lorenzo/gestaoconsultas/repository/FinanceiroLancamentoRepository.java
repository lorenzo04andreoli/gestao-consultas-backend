package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinanceiroLancamentoRepository extends JpaRepository<FinanceiroLancamento, Long> {

    boolean existsByConsultaId(Long consultaId);

    List<FinanceiroLancamento> findAllByOrderByDataCriacaoDesc();
}
