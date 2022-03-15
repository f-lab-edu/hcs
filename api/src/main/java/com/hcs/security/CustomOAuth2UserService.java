package com.hcs.security;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration : " + userRequest.getClientRegistration());
        log.info("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        log.info("getAttributes : " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("provider : " + provider);

        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        log.info("oAuth2UserInfo : " + oAuth2UserInfo);

        String nickname = oAuth2UserInfo.getNickName();
        String email = oAuth2UserInfo.getEmail();
        String password = "oauth2pwd";

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        User userEntity = userService.findByEmail(email);

        if (userEntity == null) { // 회원가입 시키기
            SignUpDto signUpDto = new SignUpDto(email, nickname, password);
            userEntity = userService.saveNewUser(signUpDto);

            log.info("sign up success!");
            log.info("user : " + userEntity);
        }

        return new UserContext(userEntity, oAuth2User.getAttributes(), roles);
    }
}
