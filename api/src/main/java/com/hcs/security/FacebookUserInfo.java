package com.hcs.security;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo {

    Map<String, Object> attributes;

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "Facebook";
    }

    @Override
    public String getEmail() {
        return "f_" + attributes.get("email").toString();
    }

    @Override
    public String getNickName() {
        return "f_" + attributes.get("name").toString();
    }
}
