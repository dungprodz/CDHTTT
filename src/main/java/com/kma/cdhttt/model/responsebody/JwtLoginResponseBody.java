package com.kma.cdhttt.model.responsebody;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class JwtLoginResponseBody implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String token;
    private String status;

    public JwtLoginResponseBody(String token, String status) {
        this.token = token;
        this.status = status;
    }
}
