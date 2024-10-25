package com.kinal.libreria_online.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "prestamos")
public class Prestamo {

    @Id
    private String id;
    private String isbn;
    private String nombreLibro;
    private String usuario;
    private Date fechaInicioPrestamo;
    private Date fechaFinPrestamo;

}
