package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Data : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
 */

@Data
@Entity
@Table(name = "User")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "emailVerified")
    private Boolean emailVerified;

    @Column(name = "emailCheckToken")
    private String emailCheckToken;

    @Column(name = "emailCheckTokenGeneratedAt")
    private LocalDateTime emailCheckTokenGeneratedAt;

    @Column(name = "joinedAt")
    private LocalDateTime joinedAt;

    @Column(name = "age")
    private Integer age;

    @Column(name = "position")
    private String position;

    @Column(name = "location")
    private String location;
}
