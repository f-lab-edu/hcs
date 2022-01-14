package com.hcs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    METHOD_ARGUMENT_NOT_VALID(400, -1, "request body로 전송된 데이터가 검증단계를 통과하지 못함"),
    NUMBER_FORMAT(400, -2, "숫자형으로 요청해주세요"),
    CLUB_ACCESS_DENIED(400,-3,"접근 권한이 없습니다.");

    private int status;
    private int errorCode;
    private String message;
}
