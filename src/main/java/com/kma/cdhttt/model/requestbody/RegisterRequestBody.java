package com.kma.cdhttt.model.requestbody;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterRequestBody {
    @NotEmpty(message = "Empty fullName")
    private String fullName;
    @NotEmpty(message = "Empty userName")
    private String userName;
    @NotEmpty(message = "Empty passWord")
    private String passWord;
    @NotEmpty(message = "Empty email")
    private String email;
    @NotEmpty(message = "Empty phoneNumber")
    private String phoneNumber;
}
