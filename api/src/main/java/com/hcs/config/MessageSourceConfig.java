package com.hcs.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * MessageSourceConfig : 에러 메시지를 추가시켜 JSON 리턴하는데 사용됨.
 */

@Configuration
public class MessageSourceConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource(); // 메시지 소스를 리로딩하여 읽음
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10); // 리로딩한 설정 파일을 10초간 캐싱함
        return messageSource;
    }
}
