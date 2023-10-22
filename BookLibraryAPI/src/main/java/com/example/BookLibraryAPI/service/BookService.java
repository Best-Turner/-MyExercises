package com.example.BookLibraryAPI.service;

import com.example.BookLibraryAPI.model.Book;

import java.util.List;

public interface BookService {

    void saveBook(Book book);
    void updateBook(long id, Book book);
    void deleteBookById(long id);
    List<Book> showAllBook();
    Book getBookById(long id);

}
