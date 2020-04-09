package com.vernonliu.authserver.core.authorization.controller;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.authorization.bean.ReferenceToken;
import com.vernonliu.authserver.core.authorization.dto.AccessCodeExchangeDTO;
import com.vernonliu.authserver.core.authorization.dto.ReferenceTokenValidationDTO;
import com.vernonliu.authserver.core.authorization.service.AuthorizationService;
import com.vernonliu.authserver.core.clients.bean.TokenType;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/authorization")
public class AuthorizationController {

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/sso")
    @CrossOrigin
    public ResponseEntity<?> validateAccessCodeAndReturnSSOTokens(@RequestBody AccessCodeExchangeDTO accessCodeExchangeDTO) throws Exception {
        log.info("Attempting to exchange access token {}", accessCodeExchangeDTO.toString());
        Account account = authorizationService.validateAndDeleteAccessCode(accessCodeExchangeDTO);
        if (account != null) {
            Map<String, String> identityAndAuthorizationTokens;
            if (account.getClient().getTokenType() == TokenType.SELF_CONTAINED_TOKEN) {
                identityAndAuthorizationTokens = jwtService.generateIdentityAndAuthorizationTokens(account);
            } else {
                ReferenceToken referenceToken = authorizationService.getOrCreateReferenceToken(account);
                identityAndAuthorizationTokens = jwtService.generateIdentityAndAuthorizationTokens(account, referenceToken.getId().toString());
            }
            log.info("{} successfully exchanged access token", account.getId());
            return new ResponseEntity<>(identityAndAuthorizationTokens, HttpStatus.OK);
        }
        log.error("Invalid Access Code Exchange");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("reference-token-validation")
    @CrossOrigin
    public ResponseEntity<?> validateReferenceToken(@RequestBody ReferenceTokenValidationDTO referenceTokenValidationDTO) {
        if (authorizationService.validateReferenceToken(referenceTokenValidationDTO) == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
