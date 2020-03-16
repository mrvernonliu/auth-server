package com.vernonliu.authserver.core.clients.dao;

import com.vernonliu.authserver.core.clients.bean.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientDAO extends JpaRepository<Client, Long> {
    Client findById(UUID id);
}
