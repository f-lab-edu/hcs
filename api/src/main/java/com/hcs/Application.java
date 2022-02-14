package com.hcs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @SpringBootApplication : auto-configuration을 담당하는 어노테이션
 * @EnableCaching : 캐시 관리 기능 활성화
 **/
@EnableCaching
@MapperScan(value = {"com.hcs.mapper"})
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}



