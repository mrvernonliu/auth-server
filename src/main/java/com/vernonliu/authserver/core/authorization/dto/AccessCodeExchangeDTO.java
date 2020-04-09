package com.vernonliu.authserver.core.authorization.dto;

import com.vernonliu.authserver.core.clients.bean.FlowType;
import com.vernonliu.authserver.core.clients.bean.TokenType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class AccessCodeExchangeDTO extends GenericClientTokenDTO {
    String accessCode;

    public AccessCodeExchangeDTO (String accessCode, String clientUuid, String clientSecret) {
        super(clientUuid, clientSecret);
        this.accessCode = accessCode;
    }

}