package com.vernonliu.authserver.core.accounts.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.dao.AccountDAO;
import com.vernonliu.authserver.core.accounts.dto.NewAccountRequestDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AccountService {

    @Autowired
    ClientService clientService;
    @Autowired
    CryptographyService cryptographyService;

    @Autowired
    AccountDAO accountDAO;

    public Account createAccount(NewAccountRequestDTO newAccountRequest) throws Exception {
        Client client = clientService.getClientFromId(newAccountRequest.getClientUuid());
        if (client == null) throw new Exception("Invalid Client");
        log.info(client.toString());

        String hashedPassword = cryptographyService.generateBcryptHashAndSalt(newAccountRequest.getPassword());
        Account account = new Account(newAccountRequest, hashedPassword, client);
        try {
            accountDAO.save(account);
            return account;
        } catch (Exception e) {
            log.error("Failed to create account");
            throw e;
        }
     }

     public Account getAccount(String email, Client client) throws Exception {
        if (client == null) throw new Exception("Invalid client");
        return accountDAO.findByEmailAndClient(email, client);
     }

     public void updateAccount(Account account) {
        account.setLastLoggedIn(new Date());
        accountDAO.save(account);
     }
}
