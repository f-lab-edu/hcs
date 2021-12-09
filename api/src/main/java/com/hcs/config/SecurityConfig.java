package com.hcs.config;

/*
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
*/
/**
 * @Configuration : spring에 "빈 팩토리를 위한 오브젝트 설정을 담당하는 클래스" 임을 알려주는 어노테이션
 * @EnableWebSecurity : WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을 달면 SpringSecurityFilterChain이 자동으로 포함됩니다.
 */

/* security 설정 이후 코드 사용 예정
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
*/
    /**
     * configure() 를 오버라이딩하여 접근 권한을 작성함.
     */
    /*
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/check-email", "/sign-up", "/check-email-token",
                        "/email-login", "/check-email-login", "/login-link","/club/*").permitAll()
                .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll()
                .mvcMatchers(HttpMethod.POST, "/sign-up").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated();
    }
}
*/
