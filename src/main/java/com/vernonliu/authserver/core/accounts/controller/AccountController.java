package com.vernonliu.authserver.core.accounts.controller;

import com.vernonliu.authserver.core.accounts.dto.NewAccountRequestDTO;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/account")
public class AccountController {

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
}
