package com.vernonliu.authserver.core.cryptography.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.clients.bean.Client;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    @Autowired
    CryptographyService cryptographyService;

    public String createSSOToken(Account account, Client client) throws Exception {
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String jwt = Jwts.builder()
                .setSubject(account.getEmail())
                .signWith(privateKey)
                .compact();
        // DO MORE THINGS
        return jwt;
    }
}