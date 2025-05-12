package com.accenture.app.javabooks.integrationTest;

import com.accenture.app.javabooks.dao.BookDao;
import com.accenture.app.javabooks.model.Book;
import com.accenture.app.javabooks.model.dto.BookRequest;
import com.accenture.app.javabooks.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookDao.deleteAll();
        Book book1 = Book.builder().titulo("El Señor de los Anillos").autor("J.R.R. Tolkien").build();
        Book book2 = Book.builder().titulo("Cien años de soledad").autor("Gabriel García Márquez").build();
        Book book3 = Book.builder().titulo("El amor en los tiempos del cólera").autor("Gabriel García Márquez").build();
        bookDao.saveAll(List.of(book1, book2, book3));
    }

    @Test
    void getBookBytitulo_existingtitulo_shouldReturnOkAndBookResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/titulo/El Señor de los Anillos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("El Señor de los Anillos"));
    }

    @Test
    void getBookBytitulo_nonExistingtitulo_shouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/titulo/Fundación"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooksByautor_existingautor_shouldReturnOkAndListOfBookResponses() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/autor/Gabriel García Márquez"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].autor").value("Gabriel García Márquez"))
                .andExpect(jsonPath("$[1].autor").value("Gabriel García Márquez"));
    }

    @Test
    void getBooksByautor_nonExistingautor_shouldReturnOkAndEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/autor/Isaac Asimov"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createBook_validInput_shouldReturnCreatedAndBookResponse() throws Exception {
        BookRequest newBook = BookRequest.builder()
                .titulo("1984")
                .autor("George Orwell")
                .cantidadPaginas(256)  
                .categoria("Dystopian") 
                .contenido("It was a bright cold day in April, and the clocks were striking thirteen.") 
                .build();
        String bookRequestJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("1984"))
                .andExpect(jsonPath("$.autor").value("George Orwell"));
    }

    @Test
    void createBook_existingtitulo_shouldReturnConflict() throws Exception {
        BookRequest existingBook = BookRequest.builder()
                .titulo("El Señor de los Anillos")
                .autor("J.R.R. Tolkien")
                .cantidadPaginas(1178) 
                .categoria("Fantasy")    
                .contenido("The Fellowship of the Ring set out from Rivendell...") 
                .build();
        String bookRequestJson = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteBook_existingId_shouldReturnNoContent() throws Exception {
        Book bookToDelete = bookDao.findByTitulo("El Señor de los Anillos").orElseThrow();
        long idToDelete = bookToDelete.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + idToDelete))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/titulo/El Señor de los Anillos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_nonExistingId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_invalidInput_shouldReturnBadRequestAndValidationErrors() throws Exception {
        BookRequest invalidBook = BookRequest.builder()
                .titulo("")
                .autor("")
                .cantidadPaginas(0)
                .categoria("")
                .contenido("")
                .build();

        String bookRequestJson = objectMapper.writeValueAsString(invalidBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Titulo is required")))
                .andExpect(jsonPath("$.message").value(containsString("Autor is required")))
                .andExpect(jsonPath("$.message").value(containsString("Page quantity must be at least 1")));
    }

    @Test
    void triggerUnhandledException_shouldReturnInternalServerError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test-exception/general"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(containsString("Unexpected error")));
    }

    @Test
    void createBook_dataIntegrityViolation_shouldReturnConflict() throws Exception {
        BookRequest validBook = BookRequest.builder()
                .titulo("Unique titulo")
                .autor("autor")
                .cantidadPaginas(100)
                .categoria("Fiction")
                .contenido("Some content")
                .build();

        String bookRequestJson = objectMapper.writeValueAsString(validBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Book with title 'Unique titulo' already exists."))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}