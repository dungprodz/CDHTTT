package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.service.RegisterService;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.ErrorCode;
import com.kma.cdhttt.ulti.KMAException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

import static com.kma.cdhttt.ulti.Validate.isPasswordValid;

@Service
@Slf4j
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
        try {
            log.info("{} register RegisterRequestBody {}", getClass().getSimpleName(), requestBody);
            RegisterResponseBody responseBody = new RegisterResponseBody();
            if (StringUtils.isEmpty(requestBody.getUserName()) || StringUtils.isEmpty(requestBody.getPassWord()) || StringUtils.isEmpty(requestBody.getEmail())
                    || StringUtils.isEmpty(requestBody.getPhoneNumber()) || StringUtils.isEmpty(requestBody.getFullName())) {
                throw new KMAException(ErrorCode.BAD_REQUEST, "BAD_REQUEST");
            }
            if(!isPasswordValid(requestBody.getPassWord())){
                throw new KMAException(ErrorCode.PASS_WORD_NOT_STRONG, "PASS_WORD_NOT_STRONG");
            }
            if (userRepository.existsByUserName(requestBody.getUserName())) {
                throw new KMAException(ErrorCode.USER_EXITS, "USER_EXITS");
            }
            UserEntity user = new UserEntity();

            user.setId(UUID.randomUUID().toString());
            user.setUserName(requestBody.getUserName());
            user.setPassWord(passwordEncoder.encode(requestBody.getPassWord()));
            user.setStatus(Common.ACTIVE);
            user.setEmail(requestBody.getEmail());
            user.setFullName(requestBody.getFullName());
            user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            user.setPhoneNumber(requestBody.getPhoneNumber());
            user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            user.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            user.setOtpCount("0");
            userRepository.save(user);

            responseBody.setStatus(Common.SUCCESS);
            return responseBody;
        }catch (Exception e){
            log.info("{} register Exception {}", getClass().getSimpleName(), e);
            throw new KMAException(ErrorCode.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
        }
    }
}
