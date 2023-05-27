package com.example.telegramBot.services.impl;

import com.example.telegramBot.models.User;
import com.example.telegramBot.repositories.UserRepository;
import com.example.telegramBot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUserByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }
}
