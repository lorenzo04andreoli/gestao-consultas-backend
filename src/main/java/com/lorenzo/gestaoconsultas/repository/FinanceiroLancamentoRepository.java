package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroLancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceiroLancamentoRepository extends JpaRepository<FinanceiroLancamento, Long> {

    boolean existsByConsultaId(Long consultaId);
}
