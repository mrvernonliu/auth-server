package com.vernonliu.authserver.core.cryptography.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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
}
