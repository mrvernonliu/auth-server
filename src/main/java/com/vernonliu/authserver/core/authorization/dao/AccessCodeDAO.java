package com.vernonliu.authserver.core.authorization.dao;

import com.vernonliu.authserver.core.authorization.bean.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCodeDAO extends JpaRepository<AccessCode, String> {

}
