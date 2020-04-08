package com.vernonliu.authserver.core.clients.service;

import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.bean.RegistrationCode;
import com.vernonliu.authserver.core.clients.dao.ClientDAO;
import com.vernonliu.authserver.core.clients.dao.RegistrationCodeDAO;
import com.vernonliu.authserver.core.clients.dto.ClientRegistrationDTO;
import com.vernonliu.authserver.core.cryptography.service.CryptographyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class RegistrationService {

    @Autowired
    ClientDAO clientDAO;
    @Autowired
    RegistrationCodeDAO registrationCodeDAO;
    @Autowired
    CryptographyService cryptographyService;

    public Client registerNewClient(ClientRegistrationDTO clientRegistrationDTO) throws Exception{
        RegistrationCode registrationCode = validateRegistrationCode(clientRegistrationDTO.getRegistration_code());
        if (registrationCode == null) {
            throw new Exception("Registration code is invalid");
        }
        Client newClient = new Client(clientRegistrationDTO, registrationCode, cryptographyService.generateRandomToken());
        cryptographyService.generateAsymmetricalKeys(newClient);
        clientDAO.save(newClient);
        registrationCode.setRegistered(true);
        registrationCodeDAO.save(registrationCode);
        return newClient;
    }

    public RegistrationCode generateNewRegistrationCode() {
        RegistrationCode registrationCode = new RegistrationCode();
        registrationCodeDAO.save(registrationCode);
        log.info(registrationCode.toString());
        return registrationCodeDAO.findById(registrationCode.getId());
    }

    public RegistrationCode validateRegistrationCode(String registrationCode) {
        RegistrationCode currentRegistrationCodeState = registrationCodeDAO.findById(UUID.fromString(registrationCode));
        if (currentRegistrationCodeState == null || currentRegistrationCodeState.isRegistered()) {
            log.error("Registration code: {} - doesn't exist or is expired!", registrationCode);
            return null;
        }
        return currentRegistrationCodeState;
    }
}
