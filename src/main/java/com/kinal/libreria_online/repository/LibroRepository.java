package com.kinal.libreria_online.repository;

import com.kinal.libreria_online.model.Libro;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LibroRepository extends MongoRepository<Libro, String> {

    Optional<Libro> findByIsbn(String isbn);

}
