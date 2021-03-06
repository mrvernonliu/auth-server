package com.vernonliu.authserver.core.clients.service;

import com.vernonliu.authserver.core.authorization.dto.GenericClientTokenDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.dao.ClientDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ClientService {

    @Autowired
    ClientDAO clientDAO;

    public Client getClientFromId(String clientUuid) {
        Client client = clientDAO.findById(UUID.fromString(clientUuid));
        log.info(client.toString());
        return client;
    }

    public String getClientNameFromId(String clientUuid) {
        String clientName = clientDAO.findById(UUID.fromString(clientUuid)).getClientName();
        log.info("Getting information: " + clientUuid + " - " + clientName);
        return clientName;
    }

    public boolean validateClientCredentials(GenericClientTokenDTO token) {
        Client client = clientDAO.findById(UUID.fromString(token.getClientUuid()));
        return client.getClientSecret().equals(token.getClientSecret());
    }
}
