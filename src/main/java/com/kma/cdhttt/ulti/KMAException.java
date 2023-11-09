package com.kma.cdhttt.ulti;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class KMAException extends Exception{
    private String statusCode;
    private String message;

    public KMAException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
