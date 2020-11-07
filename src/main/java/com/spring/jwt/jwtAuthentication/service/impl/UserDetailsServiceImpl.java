package com.spring.jwt.jwtAuthentication.service.impl;

import com.spring.jwt.jwtAuthentication.Constants.ErrorConstants;
import com.spring.jwt.jwtAuthentication.models.User;
import com.spring.jwt.jwtAuthentication.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(ErrorConstants.USER_NOT_FOUND_ERROR_CODE);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.emptyList());
    }
}
