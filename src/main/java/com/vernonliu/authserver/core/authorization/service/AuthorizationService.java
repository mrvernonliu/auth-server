package com.vernonliu.authserver.core.authorization.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authorization.dao.AccessCodeDAO;
import com.vernonliu.authserver.core.authorization.bean.AccessCode;
import com.vernonliu.authserver.core.authorization.dto.AccessCodeExchangeDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthorizationService {

    @Autowired
    CryptographyService cryptographyService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    AccessCodeDAO accessCodeDAO;

    // TODO: This class is susceptible to accessCode ID collisions, fix later
    public String createAccessCode(Account account) throws Exception {
        log.info("Creating a new access code");
        String accessCodeString = cryptographyService.generateRandomToken();
        AccessCode accessCode = new AccessCode(accessCodeString, account);
        accessCodeDAO.save(accessCode);
        return accessCodeString;
    }

    public Account validateAccessCode(AccessCodeExchangeDTO accessCodeExchangeDTO) {
        Optional<AccessCode> opt = accessCodeDAO.findById(accessCodeExchangeDTO.getAccessCode());
        if (opt.isEmpty()) return null;
        AccessCode accessCode = opt.get();
        // TODO: check if expired
        return accessCode.getAccount();
    }
}
