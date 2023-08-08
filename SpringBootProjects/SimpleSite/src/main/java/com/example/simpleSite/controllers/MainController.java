package com.example.simpleSite.controllers;

import com.example.simpleSite.models.Message;
import com.example.simpleSite.repositories.MessageRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String addMessage(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        messageRepo.save(new Message(text, tag));
        return "redirect:/main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.put("messages", messages);
        return "main";
    }

}
