package com.example.simpleSite.service;

import com.example.simpleSite.models.Message;
import com.example.simpleSite.models.User;
import com.example.simpleSite.repositories.MessageRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MessageService {

    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<Message> messageList(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter);
        } else {
            return (List<Message>) messageRepo.findAll();
        }
    }

    public Set<Message> messageListForUser(User author) {
        return messageRepo.findByUser(author);
    }
}
