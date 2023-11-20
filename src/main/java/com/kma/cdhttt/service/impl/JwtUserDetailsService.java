package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.TokenEntity;
import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.entity.UserRoleEntity;
import com.kma.cdhttt.model.requestbody.CreateNewTokenRequestBody;
import com.kma.cdhttt.model.responsebody.JwtLoginResponseBody;
import com.kma.cdhttt.repository.TokenRepository;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.repository.UserRoleRepository;
import com.kma.cdhttt.service.TokenService;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService, TokenService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository, TokenRepository tokenRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.tokenRepository = tokenRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findAllByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<String> roleList = userRoleRepository.findByUserId(user.getId()).stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        List<GrantedAuthority> grantList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (String role : roleList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(),
                grantList);
    }

    @Override
    public TokenEntity createRefreshToken(UserDetails userDetails, String token) {
        TokenEntity refreshToken = TokenEntity.builder()
                .userId(userRepository.findAllByUserName(userDetails.getUsername()).getId())
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000 * 1000))
                .token(token)
                .status(Common.ACTIVE)
                .build();
        return tokenRepository.save(refreshToken);
    }

    @Override
    public Boolean verifyExpiration(TokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            token.setStatus(Common.INACTIVE);
            tokenRepository.save(token);
            return false;
        }
        return true;
    }

    @Override
    public JwtLoginResponseBody createNewToken(CreateNewTokenRequestBody refreshTokenRequest) {
        Optional<TokenEntity> token = tokenRepository.findByRefreshToken(refreshTokenRequest.getRefreshToken());

        if (token.isPresent() && verifyExpiration(token.get())) {
            Optional<UserEntity> user = userRepository.findById(token.get().getUserId());

            if (user.isPresent()) {
                final UserDetails userDetails = loadUserByUsername(user.get().getUserName());

                String newToken = jwtTokenUtil.generateToken(userDetails);

                token.get().setToken(newToken);

                tokenRepository.save(token.get());

                return JwtLoginResponseBody.builder()
                        .jwttoken(newToken)
                        .refreshToken(token.get().getRefreshToken())
                        .status(Common.SUCCESS)
                        .build();
            } else {
                return JwtLoginResponseBody.builder().status(Common.FAIL).build();
            }
        } else {
            return JwtLoginResponseBody.builder().status(Common.FAIL).build();
        }
    }
}
