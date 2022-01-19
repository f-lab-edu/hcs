package com.hcs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyDto extends SignUpDto {

    @NotNull
    @Range(min = 15, max = 100, message = "15~100세 까지 사용이 가능합니다.")
    private int age;

    @Length(min = 3, max = 10)
    private String position;

    @NotBlank
    @Length(min = 3, max = 15)
    private String location;

    @Builder(builderMethodName = "userModifyDtoBuilder")
    public UserModifyDto(String email, String nickname, String password, int age, String position, String location) {
        super(email, nickname, password);
        this.age = age;
        this.position = position;
        this.location = location;
    }
}
