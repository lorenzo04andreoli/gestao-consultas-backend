package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import com.lorenzo.gestaoconsultas.entity.Paciente;
import com.lorenzo.gestaoconsultas.enums.StatusConsulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("""
    SELECT COUNT(c) > 0 FROM Consulta c
    WHERE c.dentista = :dentista
    AND c.status <> :statusIgnorado
    AND c.dataInicio < :fim
    AND c.dataFim > :inicio
    """)
    boolean existeConflito(
            @Param("dentista") Dentista dentista,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusIgnorado") StatusConsulta statusIgnorado
    );

    @Query("""
    SELECT COUNT(c) > 0 FROM Consulta c
    WHERE c.paciente = :paciente
    AND c.status <> :statusIgnorado
    AND c.dataInicio < :fim
    AND c.dataFim > :inicio
    """)
    boolean existeConflitoPaciente(
            @Param("paciente") Paciente paciente,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusIgnorado") StatusConsulta statusIgnorado
    );

    @Query("""
    SELECT COUNT(c) > 0 FROM Consulta c
    WHERE c.id <> :consultaId
    AND c.dentista = :dentista
    AND c.status <> :statusIgnorado
    AND c.dataInicio < :fim
    AND c.dataFim > :inicio
    """)
    boolean existeConflitoAoEditar(
            @Param("consultaId") Long consultaId,
            @Param("dentista") Dentista dentista,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusIgnorado") StatusConsulta statusIgnorado
    );

    @Query("""
    SELECT COUNT(c) > 0 FROM Consulta c
    WHERE c.id <> :consultaId
    AND c.paciente = :paciente
    AND c.status <> :statusIgnorado
    AND c.dataInicio < :fim
    AND c.dataFim > :inicio
    """)
    boolean existeConflitoPacienteAoEditar(
            @Param("consultaId") Long consultaId,
            @Param("paciente") Paciente paciente,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusIgnorado") StatusConsulta statusIgnorado
    );


    List<Consulta> findByDentistaUsuarioId(Long usuarioId);

    @Query("""
    SELECT c FROM Consulta c
    WHERE (:inicio IS NULL OR c.dataInicio >= :inicio)
    AND (:fim IS NULL OR c.dataFim <= :fim)
    """)
    List<Consulta> filtrarPorData(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByDentistaId(Long dentistaId);

    @Query("""
    SELECT c FROM Consulta c
    WHERE c.especialidade.id = :especialidadeId
    """)
    List<Consulta> filtrarPorEspecialidade(@Param("especialidadeId") Long especialidadeId);

    @Query("""
    SELECT DISTINCT c FROM Consulta c
    JOIN c.dentista d
    LEFT JOIN c.especialidade e
    WHERE (:usuarioDentistaId IS NULL OR d.usuario.id = :usuarioDentistaId)
    AND (:pacienteId IS NULL OR c.paciente.id = :pacienteId)
    AND (:dentistaId IS NULL OR d.id = :dentistaId)
    AND (:especialidadeId IS NULL OR e.id = :especialidadeId)
    AND (:usuarioId IS NULL OR c.usuario.id = :usuarioId)
    AND (:inicio IS NULL OR c.dataInicio >= :inicio)
    AND (:fim IS NULL OR c.dataFim <= :fim)
    """)
    List<Consulta> filtrarConsultas(
            @Param("usuarioDentistaId") Long usuarioDentistaId,
            @Param("pacienteId") Long pacienteId,
            @Param("dentistaId") Long dentistaId,
            @Param("especialidadeId") Long especialidadeId,
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query(
            value = """
            SELECT DISTINCT c FROM Consulta c
            JOIN c.dentista d
            JOIN c.paciente p
            LEFT JOIN c.especialidade e
            WHERE (:usuarioDentistaId IS NULL OR d.usuario.id = :usuarioDentistaId)
            AND (:status IS NULL OR c.status = :status)
            AND (:inicio IS NULL OR c.dataInicio >= :inicio)
            AND (:fim IS NULL OR c.dataInicio <= :fim)
            AND (
                :termo IS NULL OR :termo = ''
                OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(CAST(c.status AS string)) LIKE LOWER(CONCAT('%', :termo, '%'))
            )
            """,
            countQuery = """
            SELECT COUNT(DISTINCT c) FROM Consulta c
            JOIN c.dentista d
            JOIN c.paciente p
            LEFT JOIN c.especialidade e
            WHERE (:usuarioDentistaId IS NULL OR d.usuario.id = :usuarioDentistaId)
            AND (:status IS NULL OR c.status = :status)
            AND (:inicio IS NULL OR c.dataInicio >= :inicio)
            AND (:fim IS NULL OR c.dataInicio <= :fim)
            AND (
                :termo IS NULL OR :termo = ''
                OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))
                OR LOWER(CAST(c.status AS string)) LIKE LOWER(CONCAT('%', :termo, '%'))
            )
            """
    )
    Page<Consulta> buscarPaginado(@Param("usuarioDentistaId") Long usuarioDentistaId,
                                  @Param("termo") String termo,
                                  @Param("status") StatusConsulta status,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim,
                                  Pageable pageable);
}
