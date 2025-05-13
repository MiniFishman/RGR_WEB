package ru.kors.springsecurityexample.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kors.springsecurityexample.dto.BookDto;
import ru.kors.springsecurityexample.models.Books;
import ru.kors.springsecurityexample.services.BookService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(
            value = "/add-book",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Books> addBook(
            @RequestPart("book") String bookJson,
            @RequestPart("image") MultipartFile image) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        BookDto bookDto = mapper.readValue(bookJson, BookDto.class);

        Books savedBook = bookService.addBook(bookDto, image);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Books> updateBook(
            @PathVariable Integer id,
            @RequestPart("book") String bookJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        BookDto bookDto = mapper.readValue(bookJson, BookDto.class);

        Books updatedBook = bookService.updateBook(id, bookDto, image);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/all-books")
    public List<Books> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Optional<Books> getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }
}
