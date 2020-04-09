package com.vernonliu.authserver.core.authorization.dao;

import com.vernonliu.authserver.core.authorization.bean.ReferenceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReferenceTokenDAO extends JpaRepository<ReferenceToken, UUID> {
}
