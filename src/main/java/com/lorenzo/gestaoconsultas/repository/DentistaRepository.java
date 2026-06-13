package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Dentista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Optional<Dentista> findByEmail(String email);
    Optional<Dentista> findByCpf(String cpf);
    Optional<Dentista> findByCro(String cro);
    Optional<Dentista> findByUsuarioId(Long usuarioId);

    @Query(
            value = """
            SELECT DISTINCT d FROM Dentista d
            LEFT JOIN d.especialidades e
            WHERE (:ativo IS NULL OR d.ativo = :ativo)
            AND (
                :termo IS NULL OR :termo = ''
                OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.email) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.cpf) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
            )
            """,
            countQuery = """
            SELECT COUNT(DISTINCT d) FROM Dentista d
            LEFT JOIN d.especialidades e
            WHERE (:ativo IS NULL OR d.ativo = :ativo)
            AND (
                :termo IS NULL OR :termo = ''
                OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.email) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.cpf) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
            )
            """
    )
    Page<Dentista> buscarPaginado(@Param("termo") String termo,
                                  @Param("ativo") Boolean ativo,
                                  Pageable pageable);

}
