package com.vernonliu.authserver.core.cryptography.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.utils.DateUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String hostname = System.getenv("AUTH_SERVER_HOST");
    private static final long HOUR = 60000;

    @Autowired
    CryptographyService cryptographyService;

    public String createSSOToken(Account account, Client client) throws Exception {
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String jwt = Jwts.builder()
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .signWith(privateKey)
                .compact();
        // DO MORE THINGS
        return jwt;
    }

    public Map<String, String> generateIdentityAndAuthorizationTokens(Account account) throws Exception{
        Client client = account.getClient();
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String ti = Jwts.builder()
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .claim("firstname", account.getFirstname())
                .claim("lastname", account.getLastname())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .signWith(privateKey)
                .compact();

        String ta = Jwts.builder() //TODO: set claims
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .claim("scope", "TODO")
                .signWith(privateKey)
                .compact();

        return Map.of("ti", ti, "ta", ta);
    }
}