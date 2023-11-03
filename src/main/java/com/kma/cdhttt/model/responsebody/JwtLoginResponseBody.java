package com.kma.cdhttt.model.responsebody;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtLoginResponseBody implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String status;

    public JwtLoginResponseBody(String jwttoken, String status) {
        this.jwttoken = jwttoken;
        this.status = status;
    }

}
