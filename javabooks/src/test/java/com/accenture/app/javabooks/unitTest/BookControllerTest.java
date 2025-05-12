package com.accenture.app.javabooks.unitTest;

import com.accenture.app.javabooks.controller.BookController;
import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;
import com.accenture.app.javabooks.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Profile("test")
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;
    @Mock
    private BookService bookService;

    @Test
    void getBookByTitle_existingTitle_shouldReturnOkAndBookResponse() {
        String title = "El Señor de los Anillos";
        BookResponse expectedResponse =BookResponse.builder()
                .id(1L)
                .titulo(title)
                .autor("J.R.R. Tolkien")
                .build();
        when(bookService.getBookByTitulo(title)).thenReturn(expectedResponse);
        BookResponse actualResponse = bookController.getBookByTitulo(title);
        assertEquals(expectedResponse, actualResponse);
        verify(bookService, times(1)).getBookByTitulo(title);
    }

    @Test
    void getBookByTitle_nonExistingTitle_shouldThrowEntityNotFoundException() {
        String title = "Cien años de soledad";
        when(bookService.getBookByTitulo(title)).thenThrow(new EntityNotFoundException("Book with title '" + title + "' not found."));
        assertThrows(EntityNotFoundException.class, () -> bookController.getBookByTitulo(title));
        verify(bookService, times(1)).getBookByTitulo(title);
    }

    @Test
    void getBooksByAuthor_existingAutor_shouldReturnOkAndListOfBookResponses() {
        String author = "Gabriel García Márquez";
        List<BookResponse> expectedResponses = Arrays.asList(
                BookResponse.builder()
                        .id(2L)
                        .titulo("Cien años de soledad")
                        .autor(author)
                        .build(),
                BookResponse.builder()
                        .id(3L)
                        .titulo("El amor en los tiempos del cólera")
                        .autor(author)
                        .build()
        );
        when(bookService.getBooksByAutor(author)).thenReturn(expectedResponses);
        List<BookResponse> actualResponses = bookController.getBooksByAutor(author);
        assertEquals(expectedResponses, actualResponses);
        verify(bookService, times(1)).getBooksByAutor(author);
    }

    @Test
    void getBooksByAuthor_nonExistingAutor_shouldReturnOkAndEmptyList() {
        String author = "Stephen King";
        when(bookService.getBooksByAutor(author)).thenReturn(List.of());
        List<BookResponse> actualResponses = bookController.getBooksByAutor(author);
        assertEquals(0, actualResponses.size());
        verify(bookService, times(1)).getBooksByAutor(author);
    }

    @Test
    void createBook_validInput_shouldReturnCreatedAndBookResponse() {
        BookRequest request = BookRequest.builder()
                .titulo("La Metamorfosis")
                .autor("Franz Kafka")
                .build();
        BookResponse expectedResponse = BookResponse.builder()
                .id(4L)
                .titulo("La Metamorfosis")
                .autor("Franz Kafka")
                .build();
        when(bookService.createBook(request)).thenReturn(expectedResponse);
        BookResponse actualResponse = bookController.createBook(request);
        assertEquals(expectedResponse, actualResponse);
        verify(bookService, times(1)).createBook(request);
    }

    @Test
    void deleteBook_existingId_shouldReturnNoContent() {
        Long id = 5L;
        doNothing().when(bookService).deleteBook(id);
        bookController.deleteBook(id);
        verify(bookService, times(1)).deleteBook(id);
    }
}