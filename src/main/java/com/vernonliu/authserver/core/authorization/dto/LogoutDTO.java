package com.vernonliu.authserver.core.authorization.dto;

import lombok.*;

@ToString
@Getter
@Setter
public class LogoutDTO extends GenericClientTokenDTO{
    String accountUuid;

    public LogoutDTO (String accountUuid, String clientUuid, String clientSecret) {
        super(clientUuid, clientSecret);
        this.accountUuid = accountUuid;
    }

}
