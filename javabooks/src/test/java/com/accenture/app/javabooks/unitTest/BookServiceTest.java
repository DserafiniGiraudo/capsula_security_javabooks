package com.accenture.app.javabooks.unitTest;

import com.accenture.app.javabooks.dao.BookDao;
import com.accenture.app.javabooks.model.Book;
import com.accenture.app.javabooks.model.dto.BookMapper;
import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;
import com.accenture.app.javabooks.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;

@Profile("test")
class BookServiceTest {

    private static BookRequest bookRequest;
    private static BookResponse bookResponse;
    private static Book book;

    @Mock
    private BookDao bookDao;
    @Mock
    private BookMapper mapper;
    @InjectMocks
    private BookServiceImpl bookService;



    @BeforeAll
    static void init(){
        bookRequest = BookRequest.builder()
                .titulo("Test Book")
                .autor("Test Author")
                .cantidadPaginas(100)
                .categoria("Test Category")
                .contenido("Test Content")
                .build();

        book =  Book.builder()
                .id(1L)
                .titulo("Test Book")
                .autor("Test Author")
                .build();

        bookResponse = BookResponse.builder()
                .id(1L)
                .titulo("Test Book")
                .autor("Test Author")
                .cantidadPaginas(100)
                .categoria("Test Category")
                .contenido("Test Content")
                .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBook_shouldReturnBookResponse_whenBookIsCreated() {
        when(bookDao.existsByTitulo(bookRequest.getTitulo())).thenReturn(false);
        when(mapper.toEntity(bookRequest)).thenReturn(book);
        when(bookDao.save(book)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        BookResponse result = bookService.createBook(bookRequest);

        assertNotNull(result);
        assertEquals(bookResponse.getTitulo(), result.getTitulo());
        assertEquals(bookResponse.getAutor(), result.getAutor());
        verify(bookDao, times(1)).existsByTitulo(bookRequest.getTitulo());
        verify(mapper, times(1)).toEntity(bookRequest);
        verify(bookDao, times(1)).save(book);
        verify(mapper, times(1)).toResponse(book);
    }

    @Test
    void createBook_shouldThrowEntityExistsException_whenBooktituloAlreadyExists() {

        when(bookDao.existsByTitulo(bookRequest.getTitulo())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> bookService.createBook(bookRequest));
        verify(bookDao, times(1)).existsByTitulo(bookRequest.getTitulo());
        verify(mapper, never()).toEntity(any());
        verify(bookDao, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getBookBytitulo_shouldReturnBookResponse_whenBookExists() {
        String titulo = "Test Book";

        when(bookDao.findByTitulo(titulo)).thenReturn(Optional.of(book));
        when(mapper.toResponse(book)).thenReturn(bookResponse);
        BookResponse result = bookService.getBookByTitulo(titulo);
        assertNotNull(result);
        assertEquals(bookResponse, result);
        verify(bookDao, times(1)).findByTitulo(titulo);
        verify(mapper, times(1)).toResponse(book);
    }

    @Test
    void getBookBytitulo_shouldThrowEntityNotFoundException_whenBookDoesNotExist() {
        String titulo = "Nonexistent Book";
        when(bookDao.findByTitulo(titulo)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookByTitulo(titulo));
        verify(bookDao, times(1)).findByTitulo(titulo);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void getBooksByAuthor_shouldReturnListOfBookResponses_whenBooksExist() {
        String author = "Test Author";
        Book book1 = Book.builder()
                .id(1L)
                .titulo("Book 1")
                .autor(author)
                .cantidadPaginas(100)
                .categoria("Test Category")
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .titulo("Book 2")
                .autor(author)
                .cantidadPaginas(100)
                .categoria("Test Category")
                .build();

        List<Book> books = List.of(book1, book2);

        BookResponse response1 = BookResponse.builder()
                .id(1L)
                .titulo("Book 1")
                .autor(author)
                .cantidadPaginas(100)
                .categoria("Test Category")
                .build();

        BookResponse response2 = BookResponse.builder()
                .id(2L)
                .titulo("Book 2")
                .autor(author)
                .cantidadPaginas(100)
                .categoria("Test Category")
                .build();

        List<BookResponse> expectedResponses = List.of(response1, response2);

        when(bookDao.findByAutor(author)).thenReturn(books);
        when(mapper.toResponse(book1)).thenReturn(response1);
        when(mapper.toResponse(book2)).thenReturn(response2);
        List<BookResponse> result = bookService.getBooksByAutor(author);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses, result);
        verify(bookDao, times(1)).findByAutor(author);
        verify(mapper, times(1)).toResponse(book1);
        verify(mapper, times(1)).toResponse(book2);
    }

    @Test
    void getBooksByAuthor_shouldReturnEmptyList_whenNoBooksExistForAuthor() {
        String author = "Nonexistent Author";
        when(bookDao.findByAutor(author)).thenReturn(List.of());
        List<BookResponse> result = bookService.getBooksByAutor(author);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookDao, times(1)).findByAutor(author);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void deleteBook_shouldCallDeleteByIdOnBookDao() {
        Long id = 1L;
        when(bookDao.existsById(id)).thenReturn(true);
        bookService.deleteBook(id);
        verify(bookDao, times(1)).deleteById(id);
    }
}