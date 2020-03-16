package com.vernonliu.authserver.core.clients.dao;

import com.vernonliu.authserver.core.clients.bean.RegistrationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistrationCodeDAO extends JpaRepository<RegistrationCode, Long> {
    RegistrationCode findById(UUID id);
}
