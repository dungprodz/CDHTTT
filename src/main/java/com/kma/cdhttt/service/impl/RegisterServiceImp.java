package com.kma.cdhttt.service.impl;

import com.kma.cdhttt.entity.RoleEntity;
import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.entity.UserRoleEntity;
import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;
import com.kma.cdhttt.repository.RoleRepository;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.repository.UserRoleRepository;
import com.kma.cdhttt.service.RegisterService;
import com.kma.cdhttt.ulti.Common;
import com.kma.cdhttt.ulti.ErrorCode;
import com.kma.cdhttt.ulti.KMAException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

import static com.kma.cdhttt.ulti.Validate.isPasswordValid;

@Service
@Slf4j
public class RegisterServiceImp implements RegisterService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterServiceImp(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<RegisterResponseBody> register(RegisterRequestBody requestBody) throws Exception {
        try {
            log.info("{} register RegisterRequestBody {}", getClass().getSimpleName(), requestBody);
            RegisterResponseBody responseBody = new RegisterResponseBody();
            if (StringUtils.isEmpty(requestBody.getUserName()) || StringUtils.isEmpty(requestBody.getPassWord()) || StringUtils.isEmpty(requestBody.getEmail())
                    || StringUtils.isEmpty(requestBody.getPhoneNumber()) || StringUtils.isEmpty(requestBody.getFullName())) {
                throw new KMAException(ErrorCode.BAD_REQUEST, "BAD_REQUEST");
            }
            if (!isPasswordValid(requestBody.getPassWord())) {
                responseBody.setMessage("mật khẩu không đủ mạnh");
                responseBody.setStatus(ErrorCode.PASS_WORD_NOT_STRONG);
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
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

            RoleEntity role = roleRepository.findByRoleName("USER");

            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setId(UUID.randomUUID().toString());
            userRoleEntity.setRoleId(role.getRoleId());
            userRoleEntity.setUserId(user.getId());
            userRoleEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            userRoleEntity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            userRoleRepository.save(userRoleEntity);

            responseBody.setStatus(Common.SUCCESS);
            responseBody.setMessage("SUCCESS");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (KMAException ex) {
            throw ex;
        } catch (Exception e) {
            throw new KMAException(ErrorCode.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
        }
    }
}

