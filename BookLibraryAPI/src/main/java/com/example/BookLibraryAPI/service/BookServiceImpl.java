package com.example.BookLibraryAPI.service;

import com.example.BookLibraryAPI.model.Book;
import com.example.BookLibraryAPI.repository.BookRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepo repository;

    public BookServiceImpl(BookRepo repository) {
        this.repository = repository;
    }


    @Override
    public void saveBook(Book book) {
        repository.save(book);
    }

    @Override
    public void updateBook(long id, Book book) {
        book.setId(id);
        repository.save(book);
    }

    @Override
    public void deleteBookById(long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Book> showAllBook() {
        return repository.findAll();
    }

    @Override
    public Book getBookById(long id) {
        return repository.findById(id).orElse(null);
    }
}
