package com.vernonliu.authserver.core.cryptography.service;

import com.vernonliu.authserver.core.clients.bean.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class CryptographyService {

    private final int LOG_ROUNDS = 12;

    // Bcrypt stores the salt in the resulting hash so it does not need to be saved separately.
    public String generateBcryptHashAndSalt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    public boolean checkPasswordMatches(String passwordToCheck, String knownPassword) {
        return BCrypt.checkpw(passwordToCheck, knownPassword);
    }

    public void generateAsymmetricalKeys(Client client) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyPairGenerator.initialize(256, random);
        KeyPair kp = keyPairGenerator.generateKeyPair();
        String publicKey = publicKeyToString(kp.getPublic());

        client.setPrivateKey(kp.getPrivate().getEncoded()); //TODO: this should be encrypted before going into DB
        client.setPublicKey(publicKey);
}

    public String generateRandomToken() throws Exception {
        byte[] randomBytes = new byte[64];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String privateKeyToString(PrivateKey key) {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN PRIVATE KEY-----\n");
        sb.append(Base64.getEncoder().encodeToString(key.getEncoded()));
        sb.append("\n-----END PRIVATE KEY-----\n");
        return sb.toString();
    }

    private String publicKeyToString(PublicKey key) {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN PUBLIC KEY-----\n");
        sb.append(Base64.getEncoder().encodeToString(key.getEncoded()));
        sb.append("\n-----END PUBLIC KEY-----\n");
        return sb.toString();
    }

    public Key privateKeyToKeyObject(byte[] key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(key);
        return keyFactory.generatePrivate(encodedKeySpec);
    }

    public Key decodePublicKey(String publicKeyPEM) throws NoSuchAlgorithmException, InvalidKeySpecException {
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("\n", "");
        log.info("publicKey: {}", publicKeyPEM);
        byte[] key = Base64.getDecoder().decode(publicKeyPEM);

        // create a key object from the bytes
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(key);
        return keyFactory.generatePublic(encodedKeySpec);
    }
}
