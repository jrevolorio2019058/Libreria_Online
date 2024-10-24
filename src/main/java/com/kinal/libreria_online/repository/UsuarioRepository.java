package com.kinal.libreria_online.repository;

import com.kinal.libreria_online.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    Usuario findByDPI(BigInteger DPI);

    boolean existsByDPI(BigInteger DPI);
}
