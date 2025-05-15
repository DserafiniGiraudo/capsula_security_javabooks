package com.accenture.app.javabooks.controller;

import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;
import com.accenture.app.javabooks.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/authorized")
    public Map<String,String> authorized(@RequestParam String code){
        return Collections.singletonMap("code", code);
    }
}