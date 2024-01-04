package com.kma.cdhttt.controller;

import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;
import com.kma.cdhttt.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/kma/v1/register")
public class RegisterController {
    private final RegisterService registerService;
    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }
    @PostMapping
    public ResponseEntity<RegisterResponseBody> register(@RequestBody RegisterRequestBody requestBody) throws Exception {
        return registerService.register(requestBody);
    }
}
