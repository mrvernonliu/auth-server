package com.vernonliu.authserver.core.authorization.dto;

import lombok.*;

@ToString
@Getter
@Setter
public class ReferenceTokenValidationDTO extends GenericClientTokenDTO{
    String accountUuid;
    String referenceToken;

    public ReferenceTokenValidationDTO (String referenceToken, String accountUuid, String clientUuid, String clientSecret) {
        super(clientUuid, clientSecret);
        this.referenceToken = referenceToken;
        this.accountUuid = accountUuid;
    }

}
