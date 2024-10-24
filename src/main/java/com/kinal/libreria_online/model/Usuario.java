package com.kinal.libreria_online.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

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
