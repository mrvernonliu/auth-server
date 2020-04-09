package com.vernonliu.authserver.core.cryptography.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.authorization.bean.RefreshToken;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.bean.TokenType;
import com.vernonliu.authserver.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
@Slf4j
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

    public Claims decodeJwt(String jwt, Client client) {
        try {
            Key publicKey = cryptographyService.decodePublicKey(client.getPublicKey());
            return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt.strip()).getBody();
        } catch (JwtException e) {
            log.error("Failed to validate JWT: {}", e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> generateIdentityAndAuthorizationTokens(Account account) throws Exception{
        Client client = account.getClient();
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String ti = generateIdentityToken(account, client, privateKey);
        String ta = generateAccessToken_SelfContained(account, client, privateKey);
        return Map.of("ti", ti, "ta", ta);
    }

    public Map<String, String> generateIdentityAndAuthorizationTokens(Account account, String referenceTokenUuid) throws Exception{
        Client client = account.getClient();
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String ti = generateIdentityToken(account, client, privateKey);
        String ta = generateAccessToken_Reference(account, client, privateKey, referenceTokenUuid);
        return Map.of("ti", ti, "ta", ta);
    }

    private String generateIdentityToken(Account account, Client client, Key privateKey) {
        return Jwts.builder()
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .claim("firstname", account.getFirstname())
                .claim("lastname", account.getLastname())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .signWith(privateKey)
                .compact();
    }

    private String generateAccessToken_SelfContained(Account account, Client client, Key privateKey) {
        return Jwts.builder() //TODO: set claims
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .claim("scope", "TODO")
                .signWith(privateKey)
                .compact();
    }

    private String generateAccessToken_Reference(Account account, Client client, Key privateKey, String referenceTokenUuid) {
        return Jwts.builder() //TODO: set claims
                .setIssuer(hostname)
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .setSubject(account.getId().toString())
                .claim("referenceToken", referenceTokenUuid)
                .signWith(privateKey)
                .compact();
    }

    public String decodeAndGetValue(String referenceToken, Client client, String key) {
        Claims claims = decodeJwt(referenceToken, client);
        if (claims == null) {
            log.error("Claims not found");
            return null;
        }
        return claims.get(key).toString();
    }

    public String generateRefreshToken(Account account) throws Exception {
        Client client = account.getClient();
        Key privateKey = cryptographyService.privateKeyToKeyObject(client.getPrivateKey());
        String jwt = Jwts.builder()
                .setIssuer(hostname)
                .setAudience(client.getClientName())
                .setSubject(account.getId().toString())
                .claim("refreshId", account.getRefreshToken())
                .setIssuedAt(new Date())
                .setExpiration(DateUtil.getDatePlusHours(3))
                .signWith(privateKey)
                .compact();
        return jwt;
    }
}