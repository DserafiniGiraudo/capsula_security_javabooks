package com.accenture.app.javabooks.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequest {

    @NotBlank(message = "Titulo is required")
    private String titulo;

    @NotBlank(message = "Autor is required")
    private String autor;

    @NotNull(message = "Cantidad de paginas is required")
    @Min(value = 1, message = "Page quantity must be at least 1")
    private Integer cantidadPaginas;

    @NotBlank(message = "Categoria is required")
    private String categoria;

    @NotBlank(message = "Contenido is required")
    private String contenido;
}