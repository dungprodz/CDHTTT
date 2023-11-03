package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.service.RegisterService;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class RegisterServiceImp implements RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponseBody register(RegisterRequestBody requestBody) throws Exception {
        RegisterResponseBody responseBody = new RegisterResponseBody();
        if (StringUtils.isEmpty(requestBody.getUserName()) || StringUtils.isEmpty(requestBody.getPassWord()) || StringUtils.isEmpty(requestBody.getEmail())
                || StringUtils.isEmpty(requestBody.getPhoneNumber()) || StringUtils.isEmpty(requestBody.getFullName())) {
            throw new Exception(ErrorCode.BAD_REQUEST);
        }
        if (userRepository.existsByUserName(requestBody.getUserName())) {
            throw new Exception(ErrorCode.USER_EXITS);
        }
        UserEntity user = new UserEntity();

        user.setId(UUID.randomUUID().toString());
        user.setUserName(requestBody.getUserName());
        user.setPassWord(passwordEncoder.encode(requestBody.getPassWord()));
        user.setStatus(Common.ACTIVE);
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        user.setPhoneNumber(requestBody.getPhoneNumber());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        responseBody.setStatus(Common.SUCCESS);
        return responseBody;
    }
}
