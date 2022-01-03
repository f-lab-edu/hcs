package com.hcs.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @Data : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 를 한번에 설정함.
 * @EqualsAndHashCode(onlyExplicitlyIncluded = true) : equals(), hashCode() 에서 사용할 필드를 명시적으로 선언해 사용하기위해 설정함.
 * @NoArgsConstructor
 * @EqualsAndHashCode.Include : 해당 애노테이션으로 지정한 필드만  equals(), hashCode() 에서 사용한다.
 */
@Data @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Club {

    @EqualsAndHashCode.Include
    @JsonIgnore
    private Long id;
    private String title;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    private String location;
    private Long categoryId;

    @JsonIgnore
    private Set<User> members = new HashSet<>();
    @JsonIgnore
    private Set<User> managers = new HashSet<>(); // 관리자를 여러명 두어 최고, 서브 관리자로 role을 나눌 예정

    // private int memberCount; 성능 개선시 사용 예정
    // private int managerCount; 성능 개선시 사용 예정

}
