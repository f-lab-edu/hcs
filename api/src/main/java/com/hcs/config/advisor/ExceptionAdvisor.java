package com.hcs.config.advisor;

import com.hcs.config.advisor.result.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * @RestControllerAdvice : @ExceptionHandler, @ModelAttribute, @InitBinder 가 적용된 메서드들에 AOP를 적용해 Controller 단에 적용하기 위해 고안된 어노테이션
 * @ExceptionHandler : 해당 예외가 발생했을 때 메서드에 정의한 로직으로 처리할 수 있음.
 */

@RestControllerAdvice
@RestController
public class ExceptionAdvisor {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationResult handleBindException(BindException bindException, Locale locale) {
        return ValidationResult.create(bindException, messageSource, locale);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ValidationResult handleIllegalArgException(IllegalArgumentException e, Locale locale) {
        return ValidationResult.create((Errors) e, messageSource, locale);
    }

}
