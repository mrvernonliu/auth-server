package com.vernonliu.authserver.core.clients.controller;

import com.vernonliu.authserver.core.clients.bean.Client;
import com.vernonliu.authserver.core.clients.bean.RegistrationCode;
import com.vernonliu.authserver.core.clients.dto.ClientRegistrationDTO;
import com.vernonliu.authserver.core.clients.service.ClientService;
import com.vernonliu.authserver.core.clients.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ClientService clientService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> clientRegistration(@RequestBody ClientRegistrationDTO clientRegistration) {
        log.info("client registration: {}", clientRegistration.toString());
        try {
            Client client = registrationService.registerNewClient(clientRegistration);
            return new ResponseEntity<>(client, HttpStatus.OK);
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

    //TODO: refactor this into a precheck for cookies
    @PostMapping("/info/{clientUuid}")
    @ResponseBody
    @CrossOrigin
    public Map<String, String> getClientInfo(@PathVariable String clientUuid) {
        return Map.of("clientName", clientService.getClientNameFromId(clientUuid));
    }
}
