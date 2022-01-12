package com.hcs.exception.result;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorDetail {

    private String objectName;
    private String field;
    private String code;
    private String message;

    public static FieldErrorDetail create(FieldError fieldError, MessageSource messageSource, Locale locale) {
        return new FieldErrorDetail(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getCode(),
                messageSource.getMessage(fieldError, locale));
    }
}
