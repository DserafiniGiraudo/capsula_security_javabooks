package com.accenture.app.javabooks.config;


import com.accenture.app.javabooks.dao.BookDao;
import com.accenture.app.javabooks.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DBInitializer {

    private final BookDao bookDao;

    @Bean
    CommandLineRunner poblarTabla() {
        return args -> {
            bookDao.saveAll(
                    Arrays.asList(
                            Book.builder()
                                    .titulo("El Señor de los Anillos")
                                    .autor("J.R.R Tolkien")
                                    .cantidadPaginas(1178)
                                    .categoria("Fantasia")
                                    .contenido("Contenido de El Senor de los Anillos")
                                    .build(),
                            Book.builder()
                                    .titulo("Cien años de soledad")
                                    .autor("Gabriel García Márquez")
                                    .cantidadPaginas(209)
                                    .categoria("Novela")
                                    .contenido("Contenido de Cien Años de soledad")
                                    .build(),
                            Book.builder()
                                    .titulo("El amor en los tiempos del cólera")
                                    .autor("Gabriel García Marquez")
                                    .cantidadPaginas(224)
                                    .categoria("Novela")
                                    .contenido("Contenido de El amor en los tiempos del cólera")
                                    .build(),
                            Book.builder()
                                    .titulo("La Metamorfosis")
                                    .autor("Franz Kafka")
                                    .cantidadPaginas(208)
                                    .categoria("Novela")
                                    .contenido("Contenido de La Metamorfosis")
                                    .build(),
                            Book.builder()
                                    .titulo("El Quijote")
                                    .autor("Miguel de Cervantes")
                                    .cantidadPaginas(1056)
                                    .categoria("Novela")
                                    .contenido("Contenido de El Quijote")
                                    .build(),
                            Book.builder()
                                    .titulo("El Principito")
                                    .autor("Antoine de Saint-Exupery")
                                    .cantidadPaginas(96)
                                    .categoria("Novela")
                                    .contenido("Contenido de El Principito")
                                    .build(),
                            Book.builder()
                                    .titulo("Rayuela")
                                    .autor("Julio Cortazar")
                                    .cantidadPaginas(224)
                                    .categoria("Novela")
                                    .contenido("Contenido de Rayuela")
                                    .build(),
                            Book.builder()
                                    .titulo("El Alquimista")
                                    .autor("Paulo Coelho")
                                    .cantidadPaginas(208)
                                    .categoria("Novela")
                                    .contenido("Contenido de El Alquimista")
                                    .build(),
                            Book.builder()
                                    .titulo("Farenheit 451")
                                    .autor("Ray Bradbury")
                                    .cantidadPaginas(224)
                                    .categoria("Novela")
                                    .contenido("Contenido de Farenheit 451")
                                    .build(),
                            Book.builder()
                                    .titulo("El Poder de la Mente")
                                    .autor("Eckhart Tolle")
                                    .cantidadPaginas(208)
                                    .categoria("Novela")
                                    .contenido("Contenido de El Poder de la Mente")
                                    .build()
                    ));
        };
    };
}