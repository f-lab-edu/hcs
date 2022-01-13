package com.hcs.exception.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResult {

    private int errorCode;
    private String message;
}
