package com.example.telegramBot.services;


import com.example.telegramBot.models.User;

public interface UserService {
    void save(User user);
    void delete(long id);
    void updateUser(User user);
    User getUserById(long userId);
    User getUserByFirstName(String firstName);

}
