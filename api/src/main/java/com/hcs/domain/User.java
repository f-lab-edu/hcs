package com.hcs.domain;

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
    private long id;
    private String email;
    private String nickname;
    private String password;
    private boolean emailVerified;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt;
    private Integer age;
    private String position;
    private String location;
    private Set<TradePost> tradePostList = new HashSet<>();
}
