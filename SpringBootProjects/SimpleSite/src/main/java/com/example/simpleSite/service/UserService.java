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

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
        User userFromDB = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользовательл не найден!"));
        return userFromDB;
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

        if (!StringUtils.isEmptyOrWhitespaceOnly(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Simple site. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activsteCode(String code) {
        Optional<User> userFromDB = userRepo.findByActivationCode(code);
        if (userFromDB.isPresent()) {
            User user = userFromDB.get();
            user.setActivationCode(null);
            userRepo.save(user);
            return true;
        }
        return false;
    }
}
