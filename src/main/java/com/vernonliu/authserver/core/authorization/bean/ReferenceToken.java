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
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "referenceTokens")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReferenceToken {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @OneToOne
    @JoinColumn(name = "accounts")
    Account account;

    @Column
    Date expirationDate;

    @Column
    String scope;

    public ReferenceToken(Account account) {
        this.account = account;
        this.expirationDate = DateUtil.getDatePlusHours(3);
        this.scope = "SCOPE_NOT_IMPLEMENTED";
    }
}
