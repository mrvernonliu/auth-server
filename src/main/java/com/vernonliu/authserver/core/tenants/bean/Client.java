package com.vernonliu.authserver.core.tenants.bean;

import com.vernonliu.authserver.core.tenants.dto.ClientRegistrationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "clients")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @Column(name = "clientName", unique = true)
    String clientName;

    @Column(name = "clientSecret", unique = true)
    @Type(type = "uuid-char")
    UUID clientSecret;

    @Column(name = "flowType")
    @Enumerated(EnumType.STRING)
    FlowType flowType;

    @Column(name = "tokenType")
    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    @Column(name = "adminEmail")
    @Email
    String adminAdmin;

    @Column(name = "createdDate")
    @CreatedDate
    Date createdDate;

    @OneToOne
    @JoinColumn(name = "registration_codes")
    RegistrationCode registrationCode;

    public Client(ClientRegistrationDTO clientDTO, RegistrationCode registrationCode) {
        this.clientName = clientDTO.getClientName();
        this.adminAdmin = clientDTO.getAdminEmail();
        this.flowType = clientDTO.getFlowType();
        this.tokenType = clientDTO.getTokenType();
        this.adminAdmin = clientDTO.getAdminEmail();
        this.registrationCode = registrationCode;
        this.clientSecret = UUID.randomUUID();
        // Do key stuff later
    }
}
