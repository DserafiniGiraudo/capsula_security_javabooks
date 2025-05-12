package com.accenture.app.javabooks.controller;

import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;
import com.accenture.app.javabooks.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/titulo/{titulo}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBookByTitulo(@PathVariable String titulo) {
        return bookService.getBookByTitulo(titulo);
    }

    @GetMapping("/autor/{autor}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getBooksByAutor(@PathVariable  String autor) {
        return bookService.getBooksByAutor(autor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}