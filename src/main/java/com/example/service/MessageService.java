package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Message create(Message message) {
        return repository.save(message);
    }

    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    public Optional<Message> getMessageById(Integer id) {
        return repository.findById(id);
    }

    public Integer deleteMessageById(Integer id) {
        repository.deleteById(id);
        return 1;
    }

    @Transactional
    public Integer update(String message_text, Integer message_id) {
        return repository.update(message_text, message_id);
    }

    public List<Message> getAllAccountMessages(Integer account_id) {
        return repository.findByPostedBy(account_id);
    }
}
