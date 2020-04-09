package com.vernonliu.authserver.core.authentication.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.authorization.service.AuthorizationService;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class AuthenticationService {

    private static final String AUTH_WEBAPP_DOMAIN = System.getenv("AUTH_WEBAPP_DOMAIN");
    private static final String AUTH_WEBAPP_ORIGIN = System.getenv("AUTH_WEBAPP_ORIGIN");

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    CryptographyService cryptographyService;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    JwtService jwtService;

    public String login(LoginRequestDTO loginRequest, HttpServletResponse response) throws Exception {
        Client client = clientService.getClientFromId(loginRequest.getClientUuid());
        Account account = accountService.getAccount(loginRequest.getEmail(), client);
        if (account == null) throw new Exception("Account not found");
        log.info(account.toString());
        if (cryptographyService.checkPasswordMatches(loginRequest.getPassword(), account.getHashedPassword())) {
            log.info("{} logged in", account.getId());
            String accessCode = authorizationService.createAccessCode(account);
            accountService.updateAccount(account);
            String jwt = jwtService.createSSOToken(account, client);
            if (!StringUtils.isEmpty(jwt)) {
                generateAuthenticationResponse(response, client, loginRequest.getRedirectUrl(), jwt);
            }
            return accessCode;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.warn("{} failed to log-in", account.getId());
        return null;
    }

    //TODO: make secure tokens
    private void generateAuthenticationResponse(HttpServletResponse response, Client client, String redirectUrl, String jwt) {
        Cookie ssoToken = new Cookie("ssoToken", jwt);
        ssoToken.setDomain(AUTH_WEBAPP_DOMAIN);
        ssoToken.setPath("/");
        ssoToken.setMaxAge(28800); //8 hours
        response.addCookie(ssoToken);
        response.setStatus(200);
        response.setHeader("Location", redirectUrl);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", redirectUrl);
    }
}
