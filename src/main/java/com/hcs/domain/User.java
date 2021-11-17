package com.hcs.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data @Builder @EqualsAndHashCode(of = "id")
@AllArgsConstructor @NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private boolean emailVerified;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt;

    private String age;
    private String position;
    private String location;

}
