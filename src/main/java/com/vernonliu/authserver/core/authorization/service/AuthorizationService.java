package com.vernonliu.authserver.core.authorization.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authorization.bean.ReferenceToken;
import com.vernonliu.authserver.core.authorization.bean.AccessCode;
import com.vernonliu.authserver.core.authorization.dao.AccessCodeDAO;
import com.vernonliu.authserver.core.authorization.dao.ReferenceTokenDao;
import com.vernonliu.authserver.core.authorization.dto.AccessCodeExchangeDTO;
import com.vernonliu.authserver.core.authorization.dto.ReferenceTokenValidationDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import com.vernonliu.authserver.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    JwtService jwtService;

    @Autowired
    AccessCodeDAO accessCodeDAO;

    @Autowired
    ReferenceTokenDao referenceTokenDao;

    // TODO: This class is susceptible to accessCode ID collisions, fix later
    public String createAccessCode(Account account) throws Exception {
        log.info("Creating a new access code");
        String accessCodeString = cryptographyService.generateRandomToken();
        AccessCode accessCode = new AccessCode(accessCodeString, account);
        accessCodeDAO.save(accessCode);
        return accessCodeString;
    }

    public Account validateAndDeleteAccessCode(AccessCodeExchangeDTO accessCodeExchangeDTO) {
        if (!clientService.validateClientCredentials(accessCodeExchangeDTO)) return null;
        Optional<AccessCode> opt = accessCodeDAO.findById(accessCodeExchangeDTO.getAccessCode());
        if (opt.isEmpty()) return null;
        AccessCode accessCode = opt.get();
        // TODO: check if expired
        Account account = accessCode.getAccount();
        accessCodeDAO.delete(accessCode);
        return account;
    }

    public ReferenceToken getOrCreateReferenceToken(Account account) {
        ReferenceToken referenceToken = account.getReferenceToken();
        if (referenceToken != null) {
            if (validateReferenceToken(referenceToken) != null) {
                referenceToken.setExpirationDate(DateUtil.getDatePlusHours(3));
            }
        } else {
            referenceToken = new ReferenceToken(account);
        }
        referenceTokenDao.save(referenceToken);
        accountService.updateAccount(account, referenceToken);
        return referenceToken;
    }

    public ReferenceToken validateReferenceToken(ReferenceTokenValidationDTO referenceTokenValidationDTO) {
        if (!clientService.validateClientCredentials(referenceTokenValidationDTO)) return null;
        Client client = clientService.getClientFromId(referenceTokenValidationDTO.getClientUuid());
        String referenceTokenUuid = jwtService.decodeAndGetValue(
                referenceTokenValidationDTO.getReferenceToken(), client,"referenceToken");
        Optional<ReferenceToken> referenceTokenOptional = referenceTokenDao.findById(UUID.fromString(referenceTokenUuid));
        if (referenceTokenOptional.isEmpty()) return null;
        ReferenceToken referenceToken = referenceTokenOptional.get();
        return validateReferenceToken(referenceToken);
    }

    private ReferenceToken validateReferenceToken(ReferenceToken referenceToken) {
        if (referenceToken.getExpirationDate().after(new Date())) {
            log.error("Reference token expired: {}", referenceToken.getId());
            return null;
        }
        log.info("Reference token validated: {}", referenceToken.getId());
        return referenceToken;
    }


}
