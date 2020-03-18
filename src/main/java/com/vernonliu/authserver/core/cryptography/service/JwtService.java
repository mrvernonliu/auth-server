package com.vernonliu.authserver.core.cryptography.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.clients.bean.Client;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Service
public class JwtService {

    private static final String hostname = System.getenv("AUTH_SERVER_HOST");
    private static final long HOUR = 60000;

    @Autowired
    CryptographyService cryptographyService;


    public String createSSOToken(Account account, Client client) throws Exception {
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        Calendar now = Calendar.getInstance();
        String jwt = Jwts.builder()
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTimeInMillis() + 3*HOUR))
                .signWith(privateKey)
                .compact();
        // DO MORE THINGS
        return jwt;
    }
}