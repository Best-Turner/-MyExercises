package com.example.simpleSite.repositories;

import com.example.simpleSite.models.Message;
import com.example.simpleSite.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepo extends CrudRepository<Message, Integer> {
    List<Message> findByTag(String tag);

    @Query("from Message m where m.author = :author")
    Set<Message> findByUser(@Param(value = "author") User author);
}
