package com.hcs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    METHOD_ARGUMENT_NOT_VALID(400, -1, "request body로 전송된 데이터가 검증단계를 통과하지 못함"),
    NUMBER_FORMAT(400, -2, "숫자형으로 요청해주세요"),
    DATABASE_ERROR(500, -3, "db access error"),
    ILLEGAL_ARGUMENT(400, -4, "잘못된 argument 입니다"),

    CLUB_ACCESS_DENIED(400, -100, "해당 club 의 manager 만 접근이 가능합니다"),
    ALREADY_JOINED_CLUB(400, -101, "이미 club 에 등록한 user 입니다");

    private int status;
    private int errorCode;
    private String message;
}
