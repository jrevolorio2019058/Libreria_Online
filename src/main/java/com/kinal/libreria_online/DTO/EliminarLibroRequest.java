package com.kinal.libreria_online.DTO;

import jakarta.persistence.Id;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EliminarLibroRequest {

    @Id

    public String ISBN;

    private boolean autorizacion;

}
