package com.hcs.dto.response.club;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ClubInfoDto extends ClubDto {
    Set<ClubUserDto> managers;
    Set<ClubUserDto> members;
}
