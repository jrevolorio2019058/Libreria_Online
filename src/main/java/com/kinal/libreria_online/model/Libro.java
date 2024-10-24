package com.kinal.libreria_online.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "libros")
public class Libro {

    @Id
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private String idioma;
    private boolean prestado;

}
