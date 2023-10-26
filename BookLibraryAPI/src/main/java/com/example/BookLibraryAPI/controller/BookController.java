package com.example.BookLibraryAPI.controller;

import com.example.BookLibraryAPI.model.Book;
import com.example.BookLibraryAPI.service.BookService;
import com.example.BookLibraryAPI.util.BookNotCreatedException;
import com.example.BookLibraryAPI.util.BookNotFoundException;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(BookController.class);

    private final BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    //Получить список всех книг
    @GetMapping
    public ResponseEntity<List<Book>> allBooks() {
        logger.info("Метод GET: получение списка всех книг");
        List<Book> books = service.showAllBook();
        if (books.isEmpty()) {
            logger.info("Список книг не получен. " + HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.info("Список книг получен. " + HttpStatus.OK);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
    }

    //Получить книгу по её ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Метод GET: Запрос книги с id = " + id);
        Book bookById = service.getBookById(id);
        if (bookById != null) {
            logger.info("Книга с id = " + id + " получена " + HttpStatus.OK);
            return new ResponseEntity<>(bookById, HttpStatus.OK);
        } else {
            logger.info("Книга с id = " + id + "не получена " + HttpStatus.BAD_REQUEST);
            throw new BookNotFoundException("Запрашиваемая книга не найдена");
        }
    }

    //Сохранение новой книги
    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody @Valid Book book, BindingResult bindingResult) {
        logger.info("Запрос на сохранение книги");
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                builder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
                builder.append("\n");
            }
            String errorMessage = builder.toString();
            logger.info("Книга не сохранена. Причина - " + errorMessage);
            throw new BookNotCreatedException(builder.toString());
        }
        logger.info("Книга сохранена");
        service.saveBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Изменение книги с указанным ID
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid Book book, BindingResult bindingResult) {
        logger.info("Метод POST: Запрос на изменение книги с id = " + id);
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
        logger.info("Метод DELETE: Запрос на удаление книги с id = " + id);
        boolean executionResult = service.deleteBookById(id);
        if (executionResult) {
            logger.info("Книга с id = " + id + " удалена");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("Ошибка удаления книги \nКнига с таким ID не найдена");
            throw new BookNotFoundException("Книга с таким ID не найдена");
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
