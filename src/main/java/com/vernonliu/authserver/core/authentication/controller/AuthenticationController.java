package com.vernonliu.authserver.core.authentication.controller;

import com.vernonliu.authserver.core.accounts.dto.NewAccountRequestDTO;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authentication")
@Slf4j
public class AuthenticationController {

    @Autowired
    AccountService accountService;

    @ResponseBody
    @PostMapping(value = "/create-account")
    public ResponseEntity<?> createAccount(@RequestBody NewAccountRequestDTO newAccountRequest) {
        try {
            log.info(newAccountRequest.toString());
            accountService.createAccount(newAccountRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @GetMapping(value = "/test", produces = "application/json")
    public String helloWorld() {
        log.info("test");
        return "hello world";
    }
}
