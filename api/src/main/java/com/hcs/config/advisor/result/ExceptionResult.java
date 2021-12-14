package com.hcs.config.advisor.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
public class ExceptionResult {
    private Timestamp timestamp;
    private String message;
    private String reason;

    public ExceptionResult( String message, String reason) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
        this.reason = reason;
    }
}
