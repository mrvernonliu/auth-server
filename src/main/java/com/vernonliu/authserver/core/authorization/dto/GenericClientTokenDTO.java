package com.vernonliu.authserver.core.authorization.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

@ToString
@Getter
@Setter
public class GenericClientTokenDTO {
    String clientUuid;
    String clientSecret;

    public GenericClientTokenDTO(String clientUuid, String clientSecret) {
        this.clientUuid = clientUuid;
        this.clientSecret = clientSecret;
    }
}
