package com.arthur.api.repository;

import com.arthur.api.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Usuario findByEmailAndPassword(String email, String password);
}
