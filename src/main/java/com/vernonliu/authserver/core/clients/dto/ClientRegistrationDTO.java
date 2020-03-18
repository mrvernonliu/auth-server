package com.vernonliu.authserver.core.clients.dto;

import com.vernonliu.authserver.core.clients.bean.FlowType;
import com.vernonliu.authserver.core.clients.bean.TokenType;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotNull;

@ToString
@Value
@AllArgsConstructor
public class ClientRegistrationDTO {
    String clientName;
    FlowType flowType;
    TokenType tokenType;
    String adminEmail;
    String redirectUrl;
    String registration_code;

}
