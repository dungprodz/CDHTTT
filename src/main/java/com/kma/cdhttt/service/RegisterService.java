package com.kma.cdhttt.service;

import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;
import org.springframework.http.ResponseEntity;

public interface RegisterService {
    ResponseEntity<RegisterResponseBody> register(RegisterRequestBody requestBody) throws Exception;
}
