package com.kma.cdhttt.service;

import com.kma.cdhttt.entity.TokenEntity;
import com.kma.cdhttt.model.requestbody.CreateNewTokenRequestBody;
import com.kma.cdhttt.model.responsebody.JwtLoginResponseBody;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    TokenEntity createRefreshToken(UserDetails userDetails, String token);

    Boolean verifyExpiration(TokenEntity token);

    JwtLoginResponseBody createNewToken(CreateNewTokenRequestBody refreshTokenRequest);
}