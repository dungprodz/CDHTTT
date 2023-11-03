package com.kma.cdhttt.service;

import com.kma.cdhttt.model.requestbody.RegisterRequestBody;
import com.kma.cdhttt.model.responsebody.RegisterResponseBody;

public interface RegisterService {
    RegisterResponseBody register(RegisterRequestBody requestBody) throws Exception;
}
