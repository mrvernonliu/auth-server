package com.vernonliu.authserver.core.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;

@AllArgsConstructor
@ToString
@Getter
public class LoginRequestDTO {
    @Email
    String email;
    String password;
    String clientUuid;
}
