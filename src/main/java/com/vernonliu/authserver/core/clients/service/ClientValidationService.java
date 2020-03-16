package com.vernonliu.authserver.core.clients.service;

import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.bean.RegistrationCode;
import com.vernonliu.authserver.core.clients.dao.ClientDAO;
import com.vernonliu.authserver.core.clients.dao.RegistrationCodeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientValidationService {

    @Autowired
    RegistrationCodeDAO registrationCodeDAO;
    @Autowired
    ClientDAO clientDAO;

    public RegistrationCode validateRegistrationCode(String registrationCode) {
        RegistrationCode currentRegistrationCodeState = registrationCodeDAO.findById(UUID.fromString(registrationCode));
        if (currentRegistrationCodeState.isRegistered()) return null;
        return currentRegistrationCodeState;
    }

    public Client validateClientUuid(String uuid) {
        return clientDAO.findById(UUID.fromString(uuid));
    }


}
