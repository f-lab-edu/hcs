package com.hcs.dto.response.club;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClubJoinDto {
    Long applicantId;
    Integer currentMembersCount;
}
