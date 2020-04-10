package com.vernonliu.authserver.core.authorization.service;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authorization.bean.ReferenceToken;
import com.vernonliu.authserver.core.authorization.bean.AccessCode;
import com.vernonliu.authserver.core.authorization.bean.RefreshToken;
import com.vernonliu.authserver.core.authorization.dao.AccessCodeDAO;
import com.vernonliu.authserver.core.authorization.dao.ReferenceTokenDAO;
import com.vernonliu.authserver.core.authorization.dao.RefreshTokenDAO;
import com.vernonliu.authserver.core.authorization.dto.AccessCodeExchangeDTO;
import com.vernonliu.authserver.core.authorization.dto.LogoutDTO;
import com.vernonliu.authserver.core.authorization.dto.ReferenceTokenValidationDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import com.vernonliu.authserver.core.cryptography.service.JwtService;
import com.vernonliu.authserver.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    ReferenceTokenDAO referenceTokenDao;

    @Autowired
    RefreshTokenDAO refreshTokenDAO;

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
        Optional<ReferenceToken> referenceTokenOptional = referenceTokenDao.findById(UUID.fromString(referenceTokenValidationDTO.getReferenceToken()));
        if (referenceTokenOptional.isEmpty()) return null;
        ReferenceToken referenceToken = referenceTokenOptional.get();
        Account account = accountService.getAccount(referenceTokenValidationDTO.getAccountUuid());
        if (account == null) return null;
        if (account.getReferenceToken().getId().compareTo(referenceToken.getId()) != 0) return null;
        return validateReferenceToken(referenceToken);
    }

    private ReferenceToken validateReferenceToken(ReferenceToken referenceToken) {
        if (referenceToken.getExpirationDate().before(new Date())) {
            log.error("Reference token expired: {}", referenceToken.getId());
            return null;
        }
        log.info("Reference token validated: {}", referenceToken.getId());
        return referenceToken;
    }


    public String createRefreshToken(Account account) throws Exception {
        log.info("Making Refresh Token for accountid: {}", account.getId());
        if (account.getRefreshToken() == null
                || account.getRefreshToken()
                .getExpirationDate().before(new Date())) {
            RefreshToken refreshToken = new RefreshToken(account.getId());
            refreshTokenDAO.save(refreshToken);
            account.setRefreshToken(refreshToken);
            log.info("Created new refresh token for account {}, token: {}", account.getId(), refreshToken.getId());
        } else {
            RefreshToken refreshToken = account.getRefreshToken();
            refreshToken.setExpirationDate(DateUtil.getDatePlusHours(24));
            refreshTokenDAO.save(refreshToken);
            log.info("Updated refresh token for account {}, token: {}", account.getId(), refreshToken.getId());
        }
        accountService.updateAccount(account);
        return jwtService.generateRefreshToken(account);
    }

    public boolean logout(LogoutDTO logoutDTO) {
        if (!clientService.validateClientCredentials(logoutDTO)) return false;
        Account account = accountService.getAccount(logoutDTO.getAccountUuid());
        if (account == null) return false;
        if (!StringUtils.isEmpty(account.getReferenceToken())) {
            ReferenceToken referenceToken = account.getReferenceToken();
            account.setReferenceToken(null);
            accountService.updateAccount(account);
            referenceTokenDao.delete(referenceToken);
        }
        RefreshToken refreshToken = account.getRefreshToken();
        account.setRefreshToken(null);
        accountService.updateAccount(account);
        if (refreshToken != null) refreshTokenDAO.delete(refreshToken);
        return true;
    }
}
