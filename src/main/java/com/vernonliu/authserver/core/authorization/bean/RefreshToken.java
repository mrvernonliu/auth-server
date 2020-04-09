package com.vernonliu.authserver.core.authorization.bean;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "refreshTokens")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @Column
    Date expirationDate;

    @Column
    @NotNull
    UUID accountUuid;


    public RefreshToken(UUID accountUuid) {
        this.accountUuid = accountUuid;
        this.expirationDate = DateUtil.getDatePlusHours(24);
    }
}

