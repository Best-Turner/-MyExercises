package com.example.BookLibraryAPI.repository;

import com.example.BookLibraryAPI.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Query("select b.id from Book b where b.id=:id")
    @Transactional
    Long isExsistId(@Param("id") Long id);

}
