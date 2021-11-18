package com.hcs.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
=======
>>>>>>> 3c7deb7... UserController에 필요한 Form과 Test 코드 (+2 squashed commits)

import java.time.LocalDateTime;

/**
<<<<<<< HEAD
 * @Data : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
=======
 * @Data
 *      : @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequireArgsConstructor 등의 기능을 제공
>>>>>>> 3c7deb7... UserController에 필요한 Form과 Test 코드 (+2 squashed commits)
 */

@Data @Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
<<<<<<< HEAD
@AllArgsConstructor
=======
>>>>>>> 3c7deb7... UserController에 필요한 Form과 Test 코드 (+2 squashed commits)
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

    private Integer age;
    private String position;
    private String location;

}
=======
    private String age;
    private String position;
    private String location;

}
>>>>>>> 3c7deb7... UserController에 필요한 Form과 Test 코드 (+2 squashed commits)
