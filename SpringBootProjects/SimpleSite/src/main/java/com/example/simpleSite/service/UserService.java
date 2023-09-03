package com.example.simpleSite.service;

import com.example.simpleSite.models.Role;
import com.example.simpleSite.models.User;
import com.example.simpleSite.repositories.UserRepo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found!"));
    }

    public boolean addUser(User user) {
        Optional<User> fromDB = userRepo.findByUsername(user.getUsername());
        if (fromDB.isPresent()) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        sendMessage(user);

        return true;
    }

    public boolean activateCode(String code) {
        Optional<User> userFromDB = userRepo.findByActivationCode(code);
        if (userFromDB.isPresent()) {
            User user = userFromDB.get();
            user.setActivationCode(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public void saveUser(User user, Map<String, String> role) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(value -> value.name())
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : role.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void updateProfile(User user, String username, String password, String email) {

        if (!StringUtils.isEmptyOrWhitespaceOnly(username) && !user.getUsername().equals(username)) {
            user.setUsername(username);
        }
        boolean isEmailChange = !StringUtils.isEmptyOrWhitespaceOnly(email) && !user.getEmail().equals(email);
        if (isEmailChange) {
            user.setEmail(email);
        }

        boolean isChanged = user.getPassword().equals(password);
        if (!isChanged) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        if (isEmailChange) {
            sendMessage(user);
        }
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmptyOrWhitespaceOnly(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Simple site. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
}
