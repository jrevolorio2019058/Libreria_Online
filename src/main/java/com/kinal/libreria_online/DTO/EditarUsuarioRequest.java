package com.kinal.libreria_online.DTO;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditarUsuarioRequest {

    private BigInteger DPI;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String email;
    private String clave;
    private String telefono;
    private String direccion;

}
