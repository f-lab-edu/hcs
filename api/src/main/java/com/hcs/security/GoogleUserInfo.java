package com.hcs.security;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
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
        return "google";
    }

    @Override
    public String getEmail() {
        return "g_" + attributes.get("email").toString();
    }

    @Override
    public String getNickName() {
        return "g_" + attributes.get("name").toString();
    }
}
