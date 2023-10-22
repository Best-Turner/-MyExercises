package com.example.BookLibraryAPI.util;

public class BookNotCreated {
    private final String textException;

    public BookNotCreated(String textException) {
        this.textException = textException;
    }

    public String getTextException() {
        return textException;
    }
}
