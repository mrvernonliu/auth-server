package com.vernonliu.authserver.core.authorization.bean;

import com.vernonliu.authserver.core.accounts.bean.Account;
import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.utils.DateUtil;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "accessCodes")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AccessCode {

    @Id
    String accessCode;

    @ManyToOne
    @JoinColumn(name="client")
    Account account;

    @Column
    Date expirationDate;

    public AccessCode (String accessCode, Account account) {
        this.accessCode = accessCode;
        this.account = account;
        this.expirationDate = DateUtil.getDatePlusHours(1);
    }

}
