package com.example.simpleSiteRest.repo;


import com.example.simpleSiteRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<User, String> {
}
