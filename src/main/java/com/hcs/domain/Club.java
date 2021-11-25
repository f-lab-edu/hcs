package com.hcs.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

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
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private String location;
    private String category;


}