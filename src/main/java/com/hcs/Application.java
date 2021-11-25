package com.hcs;

<<<<<<< HEAD
=======
import org.mybatis.spring.annotation.MapperScan;
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
<<<<<<< HEAD
 * auto-configuration을 담당하는 어노테이션
 **/

=======
 * @SpringBootApplication : auto-configuration을 담당하는 어노테이션
 **/

@MapperScan(value = {"com.hcs.mapper"})
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
