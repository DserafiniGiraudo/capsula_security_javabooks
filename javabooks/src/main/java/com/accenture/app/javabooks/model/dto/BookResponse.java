package com.accenture.app.javabooks.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookResponse {
    private long id;
    private String titulo;
    private String autor;
    private Integer cantidadPaginas;
    private String categoria;
    private String contenido;
}
