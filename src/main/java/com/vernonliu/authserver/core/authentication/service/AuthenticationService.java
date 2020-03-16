package com.vernonliu.authserver.core.authentication.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    CryptographyService cryptographyService;

    // TODO: return JWT?
    public boolean login(LoginRequestDTO loginRequest) throws Exception {
        Client client = clientService.getClientFromId(loginRequest.getClientUuid());
        Account account = accountService.getAccount(loginRequest.getEmail(), client);
        if (account == null) throw new Exception("Account not found");
        log.info(account.toString());
        if (cryptographyService.checkPasswordMatches(loginRequest.getPassword(), account.getHashedPassword())) {
            log.info("{} logged in", account.getId());
            accountService.updateLastLogin(account);
            return true; //TODO
        }
        log.warn("{} failed to log-in", account.getId());
        return false; //TODO
    }


}
