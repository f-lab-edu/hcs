package com.hcs.dto.response.club;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClubResignDto {
    private long resignedId;
    private int currentMembersCount;
}
