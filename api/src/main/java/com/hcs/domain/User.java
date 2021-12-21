package com.hcs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * @Data : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    @JsonIgnore
    private Long id;
    private String email;
    private String nickname;
    @JsonIgnore
    private String password;
    private boolean emailVerified;
    @JsonIgnore
    private String emailCheckToken;
    @JsonIgnore
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt;

    private Integer age;
    private String position;
    private String location;
    @JsonIgnore
    private Set<TradePost> tradePostList = new HashSet<>();

}
