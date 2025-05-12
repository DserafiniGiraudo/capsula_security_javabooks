package com.accenture.app.javabooks.service;

import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest bookRequest);
    BookResponse getBookByTitulo(String title);
    List<BookResponse> getBooksByAutor(String author);
    void deleteBook(Long id);
}
