package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT * FROM message WHERE posted_by = :account_id", nativeQuery = true)
    List<Message> findByPostedBy(@Param("account_id") Integer account_id);

    @Modifying
    @Query(value = "UPDATE message m SET m.message_text = :message_text WHERE message_id = :message_id", nativeQuery = true)
    Integer update(@Param("message_text") String message_text, Integer message_id);
}
