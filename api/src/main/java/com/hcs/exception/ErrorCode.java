package com.hcs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NUMBER_FORMAT(400, -2, "숫자형으로 요청해주세요");

    private int status;
    private int errorCode;
    private String message;

}
