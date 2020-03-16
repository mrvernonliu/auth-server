package com.vernonliu.authserver.core.tenants.dao;

import com.vernonliu.authserver.core.tenants.bean.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientDAO extends JpaRepository<Client, Long> {
    Client findById(UUID id);
}
