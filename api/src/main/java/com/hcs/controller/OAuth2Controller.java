package com.hcs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    @GetMapping("/oauth2LoginSuccess")
    public String loginComplete(HttpServletResponse httpResponse) {


        httpResponse.setHeader("Location", "http://localhost:3090");
        httpResponse.setStatus(302);

        return "oauth2 login success!";
    }

    @GetMapping("/oauth2LoginFailure")
    public String loginFailure(HttpServletResponse httpResponse) {

        return "oauth2 login failure!";
    }
}
