package com.vernonliu.authserver.core.accounts.bean;

import com.vernonliu.authserver.core.accounts.dto.NewAccountRequestDTO;
import com.vernonliu.authserver.core.clients.bean.Client;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "accounts", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"email", "client"})})
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @Column
    String firstname;

    @Column
    String lastname;

    @Column
    @Email
    String email;

    @Column
    String hashedPassword;

    @ManyToOne
    @JoinColumn(name = "client")
    Client client;

    @Column
    @CreatedDate
    Date createdDate;

    @Column
    Date lastLoggedIn;

    // TODO: Add more fields later

    public Account(NewAccountRequestDTO newAccountRequestDTO, String hashedPassword, Client client) {
        this.firstname = newAccountRequestDTO.getFirstname();
        this.lastname = newAccountRequestDTO.getLastname();
        this.email = newAccountRequestDTO.getEmail();
        this.hashedPassword = hashedPassword;
        this.client = client;
        this.lastLoggedIn = new Date();

    }

}
