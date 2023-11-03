package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.responsebody.JwtLogoutResponseBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findAllByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(),
                new ArrayList<>());
    }

}
