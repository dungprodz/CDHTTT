package com.kma.cdhttt.model.requestbody;

import lombok.Data;

@Data
public class CreateNewTokenRequestBody {
    private String refreshToken;
}
