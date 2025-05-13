package ru.kors.springsecurityexample.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kors.springsecurityexample.dto.BookDto;
import ru.kors.springsecurityexample.models.Books;
import ru.kors.springsecurityexample.models.Categories;
import ru.kors.springsecurityexample.repository.BookRepository;
import ru.kors.springsecurityexample.repository.CategoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Books addBook(BookDto bookDto, MultipartFile image) throws IOException {
        Books book = new Books();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());

        Categories category = categoryRepository.findById(bookDto.getCategories().getId())
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));
        book.setCategories(category);

        if (image != null && !image.isEmpty()) {
            book.setImage(image.getBytes());
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Books updateBook(Integer id, BookDto bookDto, MultipartFile image) throws IOException {
                Books existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена: " + id));

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setAuthor(bookDto.getAuthor());
        existingBook.setDescription(bookDto.getDescription());
        existingBook.setPrice(bookDto.getPrice());

        if (bookDto.getCategories() != null) {
            Categories category = categoryRepository.findById(bookDto.getCategories().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));
            existingBook.setCategories(category);
        }

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            existingBook.setImage(image.getBytes());
        }

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    public Optional<Books> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    private void validateImage(MultipartFile image) throws IOException {
        if (!image.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Только фото можно загружать");
        }
        if (image.getSize() > 5_242_880 * 10) { // 50MB
            throw new IllegalArgumentException("Максимальный размер файла 50 МБ");
        }
    }
}
