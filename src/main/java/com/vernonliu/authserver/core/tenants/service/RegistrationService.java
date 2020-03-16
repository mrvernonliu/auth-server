package com.vernonliu.authserver.core.tenants.service;

import com.vernonliu.authserver.core.tenants.bean.Client;
import com.vernonliu.authserver.core.tenants.bean.RegistrationCode;
import com.vernonliu.authserver.core.tenants.dao.ClientDAO;
import com.vernonliu.authserver.core.tenants.dao.RegistrationCodeDAO;
import com.vernonliu.authserver.core.tenants.dto.ClientRegistrationDTO;
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

    private RegistrationCode validateRegistrationCode(String registrationCode) {
        RegistrationCode currentRegistrationCodeState = registrationCodeDAO.findById(UUID.fromString(registrationCode));
        if (currentRegistrationCodeState.isRegistered()) return null;
        return currentRegistrationCodeState;
    }

    public void registerNewClient(ClientRegistrationDTO clientRegistrationDTO) throws Exception{
        RegistrationCode registrationCode = validateRegistrationCode(clientRegistrationDTO.getRegistration_code());
        if (registrationCode == null) {
            throw new Exception("Registration code is invalid");
        }
        Client newClient = new Client(clientRegistrationDTO, registrationCode);
        clientDAO.save(newClient);
        registrationCode.setRegistered(true);
        registrationCodeDAO.save(registrationCode);
    }

    public RegistrationCode generateNewRegistrationCode() {
        RegistrationCode registrationCode = new RegistrationCode();
        registrationCodeDAO.save(registrationCode);
        log.info(registrationCode.toString());
        return registrationCodeDAO.findById(registrationCode.getId());
    }
}
