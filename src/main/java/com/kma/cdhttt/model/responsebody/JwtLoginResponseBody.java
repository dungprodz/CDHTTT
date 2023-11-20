package com.kma.cdhttt.model.responsebody;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class JwtLoginResponseBody implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String refreshToken;
    private final String status;

}
