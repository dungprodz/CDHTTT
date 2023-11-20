package com.kma.cdhttt.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String errorCode;
}
