package com.example.simpleSiteRest.controller;


import com.example.simpleSiteRest.model.Message;
import com.example.simpleSiteRest.model.User;
import com.example.simpleSiteRest.model.Views;
import com.example.simpleSiteRest.repo.MessageRepo;
import com.example.simpleSiteRest.repo.UserDetailsRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageRepo messageRepo;
    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public MessageController(MessageRepo messageRepo, UserDetailsRepo userDetailsRepo) {
        this.messageRepo = messageRepo;
        this.userDetailsRepo = userDetailsRepo;
    }


    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> index() {
//        userDetailsRepo.findOne(principal.getAttribute("sub")).orElseGet(()-> {
//            User user = new User();
//            user.setId(principal.getAttribute("sub"));
//            user.setName(principal.getAttribute("name"));
//            user.setEmail(principal.getAttribute("email"));
//            user.setLocale(principal.getAttribute("locale"));
//            user.set(principal.getAttribute("locale"));
//
//            userDetailsRepo.save(user);
//        });
        return messageRepo.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullName.class)
    public Message getOne(@PathVariable("id") Message message) {
        return message;
    }


    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }


    @PutMapping("{id}")
    public Message update(@PathVariable("id") Message messageFromDB,
                          @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDB, "id");
        return messageRepo.save(messageFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepo.delete(message);
    }

}
