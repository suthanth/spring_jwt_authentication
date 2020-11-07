package com.spring.jwt.jwtAuthentication.repository;

import com.spring.jwt.jwtAuthentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select user from User user where user.email = :email")
    public User findByEmail(String email);
}
