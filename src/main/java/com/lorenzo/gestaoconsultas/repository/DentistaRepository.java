package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Optional<Dentista> findByEmail(String email);
    Optional<Dentista> findByCpf(String cpf);
    Optional<Dentista> findByCro(String cro);

}
