package com.hcs.dto.response.club;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClubExpulsionDto {
    private long firedId;
    private int currentMembersCount;
}
