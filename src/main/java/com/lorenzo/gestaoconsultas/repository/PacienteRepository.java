package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByCpf(String cpf);

    @Query("""
    SELECT p FROM Paciente p
    WHERE (:ativo IS NULL OR p.ativo = :ativo)
    AND (
        :termo IS NULL OR :termo = ''
        OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
        OR LOWER(p.email) LIKE LOWER(CONCAT('%', :termo, '%'))
        OR LOWER(p.cpf) LIKE LOWER(CONCAT('%', :termo, '%'))
        OR LOWER(p.telefone) LIKE LOWER(CONCAT('%', :termo, '%'))
    )
    """)
    Page<Paciente> buscarPaginado(@Param("termo") String termo,
                                  @Param("ativo") Boolean ativo,
                                  Pageable pageable);
}
