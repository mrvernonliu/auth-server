package com.vernonliu.authserver.core.authorization.dto;

import com.vernonliu.authserver.core.clients.bean.FlowType;
import com.vernonliu.authserver.core.clients.bean.TokenType;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
@AllArgsConstructor
public class AccessCodeExchangeDTO {
    String accessCode;
    String clientUuid;
    String clientSecret;
}