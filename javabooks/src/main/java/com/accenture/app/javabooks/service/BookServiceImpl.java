package com.accenture.app.javabooks.service;

import com.accenture.app.javabooks.dao.BookDao;
import com.accenture.app.javabooks.model.Book;
import com.accenture.app.javabooks.model.dto.BookMapper;
import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.model.dto.BookResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookDao bookDao;
    private final BookMapper mapper;
    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        if (bookDao.existsByTitulo(bookRequest.getTitulo())) {
            throw new EntityExistsException("Book with title '" + bookRequest.getTitulo() + "' already exists.");
        }
        Book book = mapper.toEntity(bookRequest);
        book = bookDao.save(book);
        return mapper.toResponse(book);
    }

    @Override
    public BookResponse getBookByTitulo(String title) {
        Book book = bookDao.findByTitulo(title)
                .orElseThrow(() -> new EntityNotFoundException("Book with title '" + title + "' not found."));
        return mapper.toResponse(book);
    }

    @Override
    public List<BookResponse> getBooksByAutor(String author) {
        return bookDao.findByAutor(author).stream()
                .map(mapper::toResponse).toList();
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookDao.existsById(id)) throw new EntityNotFoundException("Book with id '" + id + "' not found.");
        bookDao.deleteById(id);
    }
}