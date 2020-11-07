package com.spring.jwt.jwtAuthentication.service.impl;

import com.spring.jwt.jwtAuthentication.Constants.ErrorConstants;
import com.spring.jwt.jwtAuthentication.exception.ApplicationException;
import com.spring.jwt.jwtAuthentication.models.User;
import com.spring.jwt.jwtAuthentication.repository.UserRepository;
import com.spring.jwt.jwtAuthentication.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public User createUser(User user) throws ApplicationException {
        if (!isValidaUser(user)) {
            throw new ApplicationException(ErrorConstants.USER_DETAILS_NOT_VALID_ERROR_CODE,
                    ErrorConstants.USER_DETAILS_NOT_VALID_ERROR_MESSAGE);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    private boolean isValidaUser(User user) {
        return user != null && StringUtils.isNotEmpty(user.getEmail()) && StringUtils.isNotEmpty(user.getPassword());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
