package com.vernonliu.authserver.core.tenants.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "registration_codes")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@ToString
public class RegistrationCode {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    @Column(name = "createdDate")
    @CreatedDate
    Date createdDate;

    @Column(name = "registered")
    boolean registered = false;

}
