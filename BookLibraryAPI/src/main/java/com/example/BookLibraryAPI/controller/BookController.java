package com.example.BookLibraryAPI.controller;

import com.example.BookLibraryAPI.model.Book;
import com.example.BookLibraryAPI.service.BookService;
import com.example.BookLibraryAPI.util.BookNotCreatedException;
import com.example.BookLibraryAPI.util.BookNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    //Получить список всех книг
    @GetMapping
    public ResponseEntity<List<Book>> allBooks() {
        List<Book> books = service.showAllBook();
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
    }

    //Получить книгу по её ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book bookById = service.getBookById(id);
        if (bookById != null) {
            return new ResponseEntity<>(bookById, HttpStatus.OK);
        } else {
            throw new BookNotFoundException("Запрашиваемая книга не найдена");
        }
    }

    //Сохранение новой книги
    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody @Valid Book book, BindingResult bindingResult) {
        if (book != null) {
            if (bindingResult.hasErrors()) {
                StringBuilder errorValidation = getErrorValidation(bindingResult);
                throw new BookNotCreatedException(errorValidation.toString());
            }
            service.saveBook(book);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Изменение книги с указанным ID
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid Book book, BindingResult bindingResult) {
        Book bookById = service.getBookById(id);
        if (bookById != null) {
            if (bindingResult.hasErrors()) {
                StringBuilder errorValidation = getErrorValidation(bindingResult);
                throw new BookNotCreatedException(errorValidation.toString());
            }
            service.updateBook(id, book);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BookNotCreatedException("Невозможно обновить книгу с указанным ID");
        }
    }

    //Удаление книги по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable Long id) {

        boolean executionResult = service.deleteBookById(id);
        if (executionResult) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new BookNotFoundException("Книги с таким ID не найдено");
        }
    }

    private StringBuilder getErrorValidation(BindingResult bindingResult) {
        StringBuilder builder = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            builder.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("\n");
        }
        return builder;
    }
}
