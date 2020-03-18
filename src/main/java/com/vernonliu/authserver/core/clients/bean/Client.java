package com.vernonliu.authserver.core.clients.bean;

import com.vernonliu.authserver.core.clients.dto.ClientRegistrationDTO;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Client {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @Column(name = "clientName", unique = true, nullable = false)
    String clientName;

    @Column(name = "clientSecret", unique = true, nullable = false)
    @Type(type = "uuid-char")
    UUID clientSecret;

    @Column(name = "flowType", nullable = false)
    @Enumerated(EnumType.STRING)
    FlowType flowType;

    @Column(name = "tokenType", nullable = false)
    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    @Column(name = "adminEmail", nullable = false)
    @Email
    String adminAdmin;

    @Column(name = "redirectUrl", nullable = false)
    @URL
    String redirectUrl;

    @Column(name = "createdDate", nullable = false)
    @CreatedDate
    Date createdDate;

    @Column(name = "privateKey", length = 512, nullable = false)
    @ToString.Exclude
    byte[] privateKey;

    @Column(name = "publicKey", columnDefinition = "TEXT", nullable = false)
    String publicKey;

    @OneToOne
    @JoinColumn(name = "registration_codes")
    RegistrationCode registrationCode;

    public Client(ClientRegistrationDTO clientDTO, RegistrationCode registrationCode) {
        this.clientName = clientDTO.getClientName();
        this.adminAdmin = clientDTO.getAdminEmail();
        this.flowType = clientDTO.getFlowType();
        this.tokenType = clientDTO.getTokenType();
        this.adminAdmin = clientDTO.getAdminEmail();
        this.redirectUrl = clientDTO.getRedirectUrl();
        this.registrationCode = registrationCode;
        this.clientSecret = UUID.randomUUID();
        // Do key stuff later
    }
}
