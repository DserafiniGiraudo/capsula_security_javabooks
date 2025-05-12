package com.accenture.app.javabooks.dao;

import com.accenture.app.javabooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {

    Optional<Book> findByTitulo(String title);
    List<Book> findByAutor(String author);
    boolean existsByTitulo(String title);
}
