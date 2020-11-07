package com.spring.jwt.jwtAuthentication.service.impl;

import com.spring.jwt.jwtAuthentication.Constants.ErrorConstants;
import com.spring.jwt.jwtAuthentication.dto.JwtDetailsDto;
import com.spring.jwt.jwtAuthentication.exception.ApplicationException;
import com.spring.jwt.jwtAuthentication.models.User;
import com.spring.jwt.jwtAuthentication.repository.UserRepository;
import com.spring.jwt.jwtAuthentication.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @Override
    public JwtDetailsDto loginUser(User user) throws ApplicationException {
        if (user == null) {
            throw new ApplicationException(ErrorConstants.USER_NOT_FOUND_ERROR_CODE,
                    ErrorConstants.USER_NOT_FOUND_ERROR_MESSAGE);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword(),new ArrayList<>()));
        if (authentication == null) {
            throw new ApplicationException(ErrorConstants.USER_NOT_FOUND_ERROR_CODE,
                    ErrorConstants.USER_NOT_FOUND_ERROR_MESSAGE);
        }
        JwtDetailsDto jwtDetailsDto = new JwtDetailsDto();
        jwtDetailsDto.setAccessToken(generateAccessToken(authentication));
        return jwtDetailsDto;
    }

    private String generateAccessToken(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
                .compact();
    }

    @Override
    public User getLoggedInUser(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String email = getEmailFromToken(token);
        return userRepository.findByEmail(email);
    }

    private String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey("SecretKeyToGenJWTs".getBytes())
                .parseClaimsJws(token.replace("Bearer",""))
                .getBody()
                .getSubject();
    }
}
