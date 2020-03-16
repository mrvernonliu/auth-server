package com.vernonliu.authserver.core.tenants.controller;

import com.vernonliu.authserver.core.tenants.bean.Client;
import com.vernonliu.authserver.core.tenants.bean.RegistrationCode;
import com.vernonliu.authserver.core.tenants.dao.ClientDAO;
import com.vernonliu.authserver.core.tenants.dao.RegistrationCodeDAO;
import com.vernonliu.authserver.core.tenants.dto.ClientRegistrationDTO;
import com.vernonliu.authserver.core.tenants.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> clientRegistration(@RequestBody ClientRegistrationDTO clientRegistration) {
        log.info("client registration: {}", clientRegistration.toString());
        try {
            registrationService.registerNewClient(clientRegistration);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/registrationCode")
    @ResponseBody()
    public RegistrationCode generateNewRegistrationCode() {
        return registrationService.generateNewRegistrationCode();
    }
}
