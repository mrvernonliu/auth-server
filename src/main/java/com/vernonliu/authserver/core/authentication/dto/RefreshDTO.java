package com.vernonliu.authserver.core.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;

@AllArgsConstructor
@ToString
@Getter
public class RefreshDTO {
    @Email
    String clientUuid;
    String redirectUrl;
}