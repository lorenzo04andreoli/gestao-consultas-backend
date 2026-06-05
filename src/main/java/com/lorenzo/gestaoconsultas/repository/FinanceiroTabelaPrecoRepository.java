package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.FinanceiroTabelaPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinanceiroTabelaPrecoRepository extends JpaRepository<FinanceiroTabelaPreco, Long> {

    List<FinanceiroTabelaPreco> findByAtivoTrueOrderByNomeAsc();

    boolean existsByNomeIgnoreCase(String nome);
}
