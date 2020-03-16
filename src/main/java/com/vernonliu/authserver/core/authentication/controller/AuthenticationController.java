package com.vernonliu.authserver.core.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("/authentication")
@Slf4j
public class AuthenticationController {


    @ResponseBody
    @GetMapping(value = "/test", produces = "application/json")
    public String helloWorld() {
        log.info("test");
        return "hello world";
    }
}
