package com.vernonliu.authserver.core.clients.service;

import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.dao.ClientDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    ClientDAO clientDAO;

    public Client getClientFromId(String clientUuid) {
        return clientDAO.findById(UUID.fromString(clientUuid));
    }
}
