package com.spring.jwt.jwtAuthentication.dto;

import java.io.Serializable;

public class JwtDetailsDto implements Serializable {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
