package com.vernonliu.authserver.core.authorization.dto;

import lombok.*;

@ToString
@Getter
@Setter
public class ReferenceTokenValidationDTO extends GenericClientTokenDTO{
    String referenceToken;

    public ReferenceTokenValidationDTO (String referenceToken, String clientUuid, String clientSecret) {
        super(clientUuid, clientSecret);
        this.referenceToken = referenceToken;
    }

}
