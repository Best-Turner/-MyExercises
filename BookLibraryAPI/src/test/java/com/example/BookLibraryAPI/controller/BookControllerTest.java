package com.example.BookLibraryAPI.controller;

import com.example.BookLibraryAPI.model.Book;
import com.example.BookLibraryAPI.service.BookService;
import com.example.BookLibraryAPI.util.BookNotCreatedException;
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
    private Book book = null;

    private static List<Book> bookList;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        book = new Book("Название книги", "Автор", new Date(2020 - 10 - 12));
        bookList = new ArrayList<>();
        bookList.add(new Book("test1", "test1", new Date(2021 - 10 - 10)));
        bookList.add(new Book("test2", "test2", new Date(2022 - 11 - 11)));
        bookList.add(new Book("test3", "test3", new Date(2023 - 12 - 12)));
    }


    @Test
    public void whenGetBookByIdThenReturnBookAndHttpStatus200() {
        long bookId = 1L;
        when(service.getBookById(bookId)).thenReturn(book);
        ResponseEntity<Book> response = controller.getBookById(bookId);

        verify(service).getBookById(bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());

    }

    @Test
    public void whenGetBookByNonExistentIdThenReturnExceptionMessage() {
        long bookId = 1L;
        when(service.getBookById(bookId)).thenReturn(null);

        try {
            controller.getBookById(bookId);
            fail("Ожидается исключение BookNotFoundException");
        } catch (BookNotFoundException ex) {
            assertTrue(ex.getMessage().contentEquals("Запрашиваемая книга не найдена"));
        }
        verify(service, times(1)).getBookById(bookId);
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
        when(service.deleteBookById(bookId)).thenReturn(false);
        try {
            controller.deleteBookById(bookId);
            fail("Ожидалось исключение BookNotFoundException");
        } catch (BookNotFoundException e) {
            assertEquals("Книга с таким ID не найдена", e.getMessage());
        }
    }

    @Test
    public void whenSaveBookReturnHttpStatus200() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(service).saveBook(book);

        ResponseEntity<Book> response = controller.saveBook(book, bindingResult);
        verify(service, times(1)).saveBook(book);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    public void whenSaveBookWithInvalidDataThenReturnExceptionMessage() {
        long bookId = 1L;
        when(service.getBookById(bookId)).thenReturn(book);
        when(bindingResult.hasErrors()).thenReturn(true);

        try {
            controller.saveBook(book, bindingResult);
            fail("Ожидалось исключение BookNotCreatedException");
        } catch (BookNotCreatedException ex) {
            assertNotNull(ex.getMessage());
        }
        verify(service, never()).saveBook(book);
    }


    @Test
    public void whenUpdateBookWithExistingBookThenReturnHttpStatus200() {
        long bookId = 1L;
        when(service.getBookById(bookId)).thenReturn(book);

        ResponseEntity<Book> response = controller.updateBook(bookId, book, bindingResult);
        verify(service).updateBook(bookId, book);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenUpdateNonExistentBookThenReturnExceptionMessage() {
        long bookId = 1L;
        when(service.getBookById(bookId)).thenReturn(null);
        try {
            controller.updateBook(bookId, book, bindingResult);
            fail("Ожидалось исключение BookNotCreatedException");
        } catch (BookNotCreatedException ex) {
            assertEquals("Невозможно обновить книгу с указанным ID", ex.getMessage());
        }
    }

    @Test
    public void whenUpdateExistingBookWithInvalidDataReturn() {
        long bookId = 1L;

        when(service.getBookById(bookId)).thenReturn(book);
        when(bindingResult.hasErrors()).thenReturn(true);
        try {
            controller.updateBook(bookId, book, bindingResult);
            fail("Ожидалось исключение BookNotCreatedException");
        } catch (BookNotCreatedException ex) {
            assertNotNull(ex.getMessage());
        }
        verify(service, never()).updateBook(bookId, book);
    }
}