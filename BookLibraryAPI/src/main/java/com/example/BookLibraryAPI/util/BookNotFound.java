package com.example.BookLibraryAPI.util;

public class BookNotFound {

    private String textException;

    public BookNotFound(String textException) {
        this.textException = textException;
    }

    public String getTextException() {
        return textException;
    }
}
