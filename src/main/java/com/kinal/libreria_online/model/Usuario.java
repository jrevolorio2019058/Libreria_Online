package com.kinal.libreria_online.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private BigInteger DPI;
    private String nombres;
    private String apellidos;
    private int edad;
    private String email;
    private String clave;
    private String telefono;
    private String direccion;
    private String role;

}
