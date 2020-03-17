package com.vernonliu.authserver.core.authentication.controller;

import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.authentication.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authentication")
@Slf4j
public class AuthenticationController {

    @Autowired
    AccountService accountService;
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginEndpoint(@RequestBody LoginRequestDTO loginRequest,
                                           @RequestParam String redirectUrl) throws Exception {
        if (StringUtils.isEmpty(redirectUrl)) throw new Exception("Missing redirectUrl");
        log.info(loginRequest.toString());
        String jwt = authenticationService.login(loginRequest);
        if (StringUtils.isEmpty(jwt)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        HttpHeaders headers = new HttpHeaders();
        headers.add("ta", jwt);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
