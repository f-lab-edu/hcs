package com.hcs.config.advisor;

import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.config.advisor.result.ValidationResult;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.dto.response.method.HcsException;
import com.hcs.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @Autowired
    private HcsResponseManager hcsResponseManager;

    @Autowired
    private HcsException hcsException;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationResult handleBindException(BindException bindException, Locale locale) {
        return ValidationResult.create(bindException, messageSource, locale);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public HcsResponse NumberFormatExceptionHandler() {
        ErrorCode error = ErrorCode.NUMBER_FORMAT;
//        return hcsResponseManager.Exception(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage()));
        return hcsResponseManager.makeHcsResponse(hcsException.exception(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage())));
    }

    // TODO 추후 Response가 만들어지면 공통으로 처리될 error에 대한 전역적인 @ExceptionHandler 추가 작성될 것임.
}
