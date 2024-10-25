package com.kinal.libreria_online.repository;

import com.kinal.libreria_online.model.Prestamo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrestamoRepository extends MongoRepository<Prestamo, String> {

    List<Prestamo> findByUsuario(String usuario);
    Prestamo findByIsbn(String isbn);
}
