package com.lorenzo.gestaoconsultas.repository;

import com.lorenzo.gestaoconsultas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findByPerfilAndAtivoTrue(String perfil);
}
