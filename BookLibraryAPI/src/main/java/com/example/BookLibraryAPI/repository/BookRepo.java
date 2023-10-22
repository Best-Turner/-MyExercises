package com.example.BookLibraryAPI.repository;

import com.example.BookLibraryAPI.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Query("SELECT id from books where id=:id")
    int idExsist(@Param("id") long id);

}
