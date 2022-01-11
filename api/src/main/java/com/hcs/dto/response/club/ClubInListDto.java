package com.hcs.dto.response.club;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @EqualsAndHashCode(callSuper = true) : equals, hashcode 메소드사용시 슈퍼클래스 값 포함
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ClubInListDto extends ClubDto {
    private Long clubId;
    private int managerCount;
    private int memberCount;
}
