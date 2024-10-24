package com.kinal.libreria_online.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditarLibroRequest {

    private String Isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private String idioma;

}
