package com.hcs.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import lombok.AllArgsConstructor;
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3

import java.time.LocalDateTime;

/**
<<<<<<< HEAD
 * @Data
 *      : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
=======
 * @Data : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3
 */

@Data @Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
<<<<<<< HEAD
=======
@AllArgsConstructor
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3
public class User {

    private Long id;
    private String email;
    private String nickname;
    private String password;
    private boolean emailVerified;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt;

<<<<<<< HEAD
    private String age;
    private String position;
    private String location;

}
=======

    private Integer age;
    private String position;
    private String location;

}
>>>>>>> 49bcb431eb1b1b947fee584b1e102ee781904fc3
