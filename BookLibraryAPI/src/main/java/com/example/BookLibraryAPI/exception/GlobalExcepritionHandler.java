package com.example.BookLibraryAPI.exception;

import com.example.BookLibraryAPI.util.BookNotCreated;
import com.example.BookLibraryAPI.util.BookNotCreatedException;
import com.example.BookLibraryAPI.util.BookNotFound;
import com.example.BookLibraryAPI.util.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExcepritionHandler {

    @ExceptionHandler
    public ResponseEntity<BookNotFound> handleBookNotFoundExceprion(BookNotFoundException ex) {
        BookNotFound notFound = new BookNotFound(ex.getMessage());
        return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<BookNotCreated> handleBookNotCreatedxceprion(BookNotCreatedException ex) {
        BookNotCreated notCreated = new BookNotCreated(ex.getMessage());
        return new ResponseEntity<>(notCreated, HttpStatus.BAD_REQUEST);
    }
}
