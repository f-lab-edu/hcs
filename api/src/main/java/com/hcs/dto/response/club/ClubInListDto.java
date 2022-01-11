package com.hcs.dto.response.club;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @EqualsAndHashCode(callSuper = true) : equals, hashcode 메소드사용시 슈퍼클래스 값 포함
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ClubInListDto extends ClubDto{
    private int managerCount;
    private int memberCount;
}
