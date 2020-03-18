package com.vernonliu.authserver.core.authentication.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    CryptographyService cryptographyService;

    @Autowired
    JwtService jwtService;

    // TODO: place redirectUrl inside of the login request?
    public void login(LoginRequestDTO loginRequest, String redirectUrl, HttpServletResponse response) throws Exception {
        Client client = clientService.getClientFromId(loginRequest.getClientUuid());
        Account account = accountService.getAccount(loginRequest.getEmail(), client);
        if (account == null) throw new Exception("Account not found");
        log.info(account.toString());
        if (cryptographyService.checkPasswordMatches(loginRequest.getPassword(), account.getHashedPassword())) {
            log.info("{} logged in", account.getId());
            accountService.updateLastLogin(account);
            String jwt = jwtService.createSSOToken(account, client);
            if (!StringUtils.isEmpty(jwt)) {
                generateAuthenticationResponse(response, client, redirectUrl, jwt);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.warn("{} failed to log-in", account.getId());
    }

    private void generateAuthenticationResponse(HttpServletResponse response, Client client, String redirectUrl, String jwt) {
        response.addCookie(new Cookie("ssoToken", jwt));
        response.setStatus(302);
        response.setHeader("Location", redirectUrl);
    }


}
