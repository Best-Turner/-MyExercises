package com.example.simpleSite.repositories;

import com.example.simpleSite.models.Message;
import com.example.simpleSite.models.User;
import com.example.simpleSite.models.dto.MessageDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepo extends CrudRepository<Message, Integer> {
    @Query("select new com.example.simpleSite.models.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    List<MessageDto> findAll(@Param("user") User user);

    @Query("select new com.example.simpleSite.models.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    List<MessageDto> findByTag(@Param("tag") String tag, @Param("user") User user);

    //    @Query("from Message m where m.author = :author")
    @Query("select new com.example.simpleSite.models.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Set<MessageDto> findByUser(@Param("author") User author, @Param("user") User user);
}
