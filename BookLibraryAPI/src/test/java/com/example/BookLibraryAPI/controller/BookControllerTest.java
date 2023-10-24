package com.example.BookLibraryAPI.controller;

import com.example.BookLibraryAPI.model.Book;
import com.example.BookLibraryAPI.service.BookService;
import com.example.BookLibraryAPI.util.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookControllerTest {

    @Mock
    private BookService service;
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BookController controller;
    private Book expectedBook = null;

    private static List<Book> bookList;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        expectedBook = new Book("Название книги", "Автор", new Date(2020 - 10 - 12));
        bookList = new ArrayList<>();
        bookList.add(new Book("test1", "test1", new Date(2021 - 10 - 10)));
        bookList.add(new Book("test2", "test2", new Date(2022 - 11 - 11)));
        bookList.add(new Book("test3", "test3", new Date(2023 - 12 - 12)));
    }


    @Test
    public void whenGetBookByIdThenReturnBookAndHttpStatus200() {
        // Ожидаемые значения
        long bookId = 1L;

        // Настройка мока для сервиса
        when(service.getBookById(bookId)).thenReturn(expectedBook);

        // Вызов метода контроллера
        ResponseEntity<Book> response = controller.getBookById(bookId);

        verify(service).getBookById(bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBook, response.getBody());

    }

    @Test
    public void whenGetListBooksThenMustReturnBooksAndStatus200() {
        when(service.showAllBook()).thenReturn(bookList);
        ResponseEntity<List<Book>> response = controller.allBooks();
        verify(service).showAllBook();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookList, response.getBody());

    }

    @Test
    public void whenBookListIsEmptyThenMustReturnHttpStatus204() {
        bookList.clear();
        when(service.showAllBook()).thenReturn(bookList);
        ResponseEntity<List<Book>> response = controller.allBooks();
        verify(service).showAllBook();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void whenDeletedBookThenMustReturnHttpStatus200() {
        long bookId = 1L;
        when(service.deleteBookById(bookId)).thenReturn(true);
        ResponseEntity<Book> response = controller.deleteBookById(bookId);
        verify(service).deleteBookById(bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenDeleteBookWithNonExistentIdThenThrowBookNotFoundException() {
        long bookId = 1L;
        when(service.deleteBookById(bookId)).thenThrow(new BookNotFoundException("Книга с таким ID не найдена"));
        try {
            verify(service).deleteBookById(bookId);
            fail("Ожидалось исключение BookNotFoundException()");
        } catch (BookNotFoundException ex) {
            verify(service).deleteBookById(bookId);
        }
    }

    @Test
    public void whenSaveBookReturnHttpStatus200() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(service).saveBook(expectedBook);

        ResponseEntity<Book> response = controller.saveBook(expectedBook, bindingResult);
        verify(service, times(1)).saveBook(expectedBook);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

}