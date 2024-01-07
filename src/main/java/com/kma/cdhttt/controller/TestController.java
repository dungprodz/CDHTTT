package com.kma.cdhttt.controller;

import com.kma.cdhttt.entity.UserEntity;
import com.kma.cdhttt.model.requestbody.Injection;
import com.kma.cdhttt.model.requestbody.JwtLoginRequestBody;
import com.kma.cdhttt.repository.UserRepository;
import com.kma.cdhttt.service.impl.UserServiceImp;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/mail")
    public List<UserEntity> findByMailWithORM(@RequestBody Injection injection) {
        List<UserEntity> user = userRepository.findAllByEmail(injection.getEmail());
        if(Objects.isNull(user)){
            return null;
        }
        return user;
    }
    @GetMapping("/ddos")
    @RateLimiter(name = "myRateLimiter", fallbackMethod = "fallbackMethod")
    public String testDDos() {
        return "SUCCESS";
    }
    public ResponseEntity<String> fallbackMethod(Throwable throwable){
        return new ResponseEntity<String>("user service does not permit further calls", HttpStatus.TOO_MANY_REQUESTS);

    }
//    public String fallbackMethod(Throwable throwable) {
//        System.out.println("Request denied due to rate limiting");
//        return "Request denied due to rate limiting";
//    }

}
