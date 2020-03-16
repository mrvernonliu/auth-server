package com.vernonliu.authserver.core.accounts.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.dao.AccountDAO;
import com.vernonliu.authserver.core.accounts.dto.NewAccountRequestDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AccountService {

    @Autowired
    ClientValidationService clientValidationService;

    @Autowired
    AccountDAO accountDAO;

    public Account createAccount(NewAccountRequestDTO newAccountRequest) throws Exception {
        Client client = clientValidationService.validateClientUuid(newAccountRequest.getClientUuid());
        if (client == null) throw new Exception("Invalid Client");
        if (!client.getClientSecret().equals(UUID.fromString(newAccountRequest.getClientSecret())))
            throw new Exception("Invalid Client Secret");
        log.info(client.toString());

        //TODO hashing service
        String hashedPassword = newAccountRequest.getPassword();

        Account account = new Account(newAccountRequest, hashedPassword, client);
        try {
            accountDAO.save(account);
            return account;
        } catch (Exception e) {
            log.error("Failed to create account");
            throw e;
        }
     }
}
