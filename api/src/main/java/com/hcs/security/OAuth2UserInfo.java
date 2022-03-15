package com.hcs.security;

import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();

    String getProviderId();

    String getProvider();

    String getEmail();

    String getNickName();
}
