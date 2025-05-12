package com.accenture.app.javabooks.model.dto;

import com.accenture.app.javabooks.model.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface BookMapper {

    Book toEntity(BookRequest request);
    BookResponse toResponse(Book book);
}