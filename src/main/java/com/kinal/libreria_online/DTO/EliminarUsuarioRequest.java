package com.kinal.libreria_online.DTO;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EliminarUsuarioRequest {

    public BigInteger DPI;

    private boolean autorizacion;

}
