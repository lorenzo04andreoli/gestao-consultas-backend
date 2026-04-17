package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Consulta;
import com.lorenzo.gestaoconsultas.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByDentistaAndDataInicioBetween(Dentista dentista, LocalDateTime inicio, LocalDateTime fim);

    List<Consulta> findByDentistaId(Long dentistaId);
}
