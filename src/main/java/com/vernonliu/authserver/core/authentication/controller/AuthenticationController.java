package com.vernonliu.authserver.core.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vernonliu.authserver.core.accounts.service.AccountService;
import com.vernonliu.authserver.core.authentication.dto.LoginRequestDTO;
import com.vernonliu.authserver.core.authentication.dto.RefreshDTO;
import com.vernonliu.authserver.core.authentication.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
    public String loginEndpoint(@RequestBody LoginRequestDTO loginRequest,
                                            HttpServletResponse response,
                                            HttpServletRequest request ) throws Exception {
        if (StringUtils.isEmpty(loginRequest.getRedirectUrl())) throw new Exception("Missing redirectUrl");
        log.info(loginRequest.toString());
        String accessCode = authenticationService.login(loginRequest, response);
        return loginRequest.getRedirectUrl() + "?accessCode=" + accessCode; // TODO: append access code as URL param
    }

    @PostMapping("/refresh")
    @ResponseBody
    public String refreshEndpoint(@RequestBody RefreshDTO refreshDTO
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("Checking refresh: {}", refreshDTO.toString());
        String accessCode = authenticationService.refreshLogin(refreshDTO, request, response);
        if (accessCode == null) response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return refreshDTO.getRedirectUrl() + "?accessCode=" + accessCode;
    }
}
