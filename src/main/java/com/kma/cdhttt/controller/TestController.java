package com.kma.cdhttt.controller;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.requestbody.Injection;
import com.kma.cdhttt.model.requestbody.JwtLoginRequestBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.service.impl.UserServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/kma/v1/user")
public class TestController {
    private final UserRepository userRepository;
    private final UserServiceImp userServiceImp;

    public TestController(UserRepository userRepository, UserServiceImp userServiceImp) {
        this.userRepository = userRepository;
        this.userServiceImp = userServiceImp;
    }

    @GetMapping
    public List<UserEntity> register() {
        return userRepository.findAll();
    }

    @PostMapping
    public List<UserEntity> findByMail(@RequestBody Injection injection) {
        List<UserEntity> user = userServiceImp.findByEmail(injection.getEmail());
        if(Objects.isNull(user)){
            return null;
        }
        return user;
    }
}
