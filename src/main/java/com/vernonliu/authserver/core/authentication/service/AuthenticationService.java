package com.vernonliu.authserver.core.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.authentication.dto.RefreshDTO;
import com.vernonliu.authserver.core.authorization.bean.RefreshToken;
import com.vernonliu.authserver.core.authorization.service.AuthorizationService;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
            String refreshToken = authorizationService.createRefreshToken(account);
            if (!StringUtils.isEmpty(refreshToken)) {
                generateAuthenticationResponse(response, client, loginRequest.getRedirectUrl(), refreshToken);
            }
            return accessCode;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.warn("{} failed to log-in", account.getId());
        return null;
    }

    public String refreshLogin(RefreshDTO refreshDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request == null || request.getCookies() == null) return null;
        Cookie refreshCookie = Arrays.stream(request.getCookies())
                .filter((cookie) -> "refresh".equals(cookie.getName()))
                .findFirst()
                .orElse(null);
        if (refreshCookie == null) return null;
        Client client = clientService.getClientFromId(refreshDTO.getClientUuid());
        String refreshJson = jwtService.decodeAndGetValue(refreshCookie.getValue(), client, "refreshId");

        // TODO: extract this
        refreshJson = refreshJson.replace("{","").replace("}","");
        HashMap<String, String> refreshMap = new HashMap<>();
        String[] pairs = refreshJson.split(",");
        for (String pair: pairs) {
            String[] values = pair.split("=");
            refreshMap.put(values[0].strip(), values[1].strip());
        }
        log.info("RefreshToken: {}", refreshMap.toString());
        String accountId = refreshMap.get("accountUuid");
        String refreshId = refreshMap.get("id");
        Account account = accountService.getAccount(accountId);
        if (refreshId == null || account == null || account.getRefreshToken() == null) return null;
        if (!refreshId.equals(account.getRefreshToken().getId().toString())) return null;

        return authorizationService.createAccessCode(account);
    }

    //TODO: make secure tokens
    private void generateAuthenticationResponse(HttpServletResponse response, Client client, String redirectUrl, String jwt) {
        Cookie refreshToken = new Cookie("refresh", jwt);
        refreshToken.setDomain(AUTH_WEBAPP_DOMAIN);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(24); //24 hours
        response.addCookie(refreshToken);
        response.setStatus(200);
        response.setHeader("Location", redirectUrl);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", redirectUrl);
    }
}
