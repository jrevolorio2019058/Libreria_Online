package com.kinal.libreria_online.DTO;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuscarLibroDTORequest {

    private String isbn;

}
