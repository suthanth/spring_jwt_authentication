package com.spring.jwt.jwtAuthentication.service;

import com.spring.jwt.jwtAuthentication.exception.ApplicationException;
import com.spring.jwt.jwtAuthentication.models.User;

import java.util.List;

public interface UserService {

    public User createUser(User user) throws ApplicationException;

    List<User> getAllUsers();
}
