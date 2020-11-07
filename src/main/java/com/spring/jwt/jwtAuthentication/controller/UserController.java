package com.spring.jwt.jwtAuthentication.controller;

import com.spring.jwt.jwtAuthentication.Constants.CommonConstants;
import com.spring.jwt.jwtAuthentication.dto.JwtDetailsDto;
import com.spring.jwt.jwtAuthentication.dto.ResponseDto;
import com.spring.jwt.jwtAuthentication.exception.ApplicationException;
import com.spring.jwt.jwtAuthentication.models.User;
import com.spring.jwt.jwtAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/v1/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/createUser")
    public @ResponseBody ResponseDto<User> createUser(@RequestBody User user) {
        ResponseDto<User> responseDto = new ResponseDto<>();
        try {
            responseDto.setData(userService.createUser(user));
            responseDto.setStatus(CommonConstants.SUCCESS);
        } catch (ApplicationException ae) {
            responseDto.setStatus(CommonConstants.FAILURE);
            responseDto.setErrorCode(ae.getErrorCode());
            responseDto.setErrorMessage(ae.getErrorMessage());
            System.out.println("Exception in createUser" + ae.getMessage());
        } catch (Exception e) {
            responseDto.setErrorMessage(e.getMessage());
            responseDto.setStatus(CommonConstants.FAILURE);
            System.out.println("Exception in createUser" + e);
        }
        return responseDto;
    }

    @GetMapping(value = "/getAllUsers")
    public @ResponseBody ResponseDto<List<User>> getAllUsers() {
        ResponseDto<List<User>> responseDto = new ResponseDto<>();
        try {
            responseDto.setData(userService.getAllUsers());
            responseDto.setStatus(CommonConstants.SUCCESS);
        } catch (Exception e) {
            responseDto.setErrorMessage(e.getMessage());
            responseDto.setStatus(CommonConstants.FAILURE);
            System.out.println("Exception in sign In" + e);
        }
        return responseDto;
    }

    @PostMapping(value = "/login")
    public @ResponseBody ResponseDto<JwtDetailsDto> loginUser(@RequestBody User user,
                                                              HttpServletResponse response) {
        ResponseDto<JwtDetailsDto> responseDto = new ResponseDto<>();
        try {
            responseDto.setData(userService.loginUser(user));
            responseDto.setStatus(CommonConstants.SUCCESS);
        } catch (ApplicationException ae) {
            responseDto.setStatus(CommonConstants.FAILURE);
            responseDto.setErrorCode(ae.getErrorCode());
            responseDto.setErrorMessage(ae.getErrorMessage());
            System.out.println("Exception in sign In" + ae.getMessage());
            response.setStatus(403);
        } catch (Exception e) {
            responseDto.setErrorMessage(e.getMessage());
            responseDto.setStatus(CommonConstants.FAILURE);
            response.setStatus(403);
            System.out.println("Exception in sign In" + e);
        }
        return responseDto;
    }
}
