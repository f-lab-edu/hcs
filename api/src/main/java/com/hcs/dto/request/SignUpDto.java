package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 각 속성들을 검증을 위해 Bean Validation 에서 제공하는 어노테이션을 활용함.
 *
 * @NotBlank : (Null, "", 공백) 체크
 * @Length : 문자열의 길이가 min 이상 max 이하인지 체크
 * @Pattern : 정규식을 만족하는지 체크
 * @Email : Email 형식인지 체크
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 3, max = 10)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{3,20}$")
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 15)
    private String password;
}

