package com.example.simpleSite.service;

import com.example.simpleSite.models.User;
import com.example.simpleSite.models.dto.MessageDto;
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

    public List<MessageDto> messageList(String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, user);
        } else {
            return messageRepo.findAll(user);
        }
    }

    public Set<MessageDto> messageListForUser(User author, User currentUser) {
        return messageRepo.findByUser(author, currentUser);
    }
}
