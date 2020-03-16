package com.vernonliu.authserver.core.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class NewAccountRequestDTO {
    String firstname;
    String lastname;
    String email;
    String username;
    String password;
    String clientUuid;
}
