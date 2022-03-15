package com.hcs.controller;

import com.hcs.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    @GetMapping("/oauth2LoginSuccess")
    public String oauth_authentication_success(@AuthenticationPrincipal UserContext user,
                                               HttpServletRequest request,
                                               HttpServletResponse response,
                                               HttpSession session) throws URISyntaxException {

        log.info("user : " + user.getUser());
        log.info("session : " + session.getId());

        Cookie loginCookie = new Cookie("email", user.getUser().getEmail());

        response.addCookie(loginCookie);
        response.setHeader("Location", "http://localhost:3090/messenger");
        response.setStatus(302);

        return "oauth2 login success!";
    }

    @GetMapping("/oauth2LoginFailure")
    public String loginFailure(HttpServletResponse httpResponse) {

        return "oauth2 login failure!";
    }
}
