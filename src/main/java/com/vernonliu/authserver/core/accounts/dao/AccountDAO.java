package com.vernonliu.authserver.core.accounts.dao;

import com.vernonliu.authserver.core.accounts.bean.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountDAO extends JpaRepository<Account, Long> {
    Account findById(UUID id);
}
