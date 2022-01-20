package com.hcs.controller.advisor;

import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.method.HcsException;
import com.hcs.exception.ErrorCode;
import com.hcs.exception.club.AlreadyJoinedClubException;
import com.hcs.exception.club.ClubAccessDeniedException;
import com.hcs.exception.global.DatabaseException;
import com.hcs.exception.result.ExceptionResult;
import com.hcs.exception.result.ValidationResult;
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
    private HcsException hcsException;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HcsResponse handleBindException(BindException bindException, Locale locale) {

        ErrorCode error = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        ValidationResult errorResults = ValidationResult.create(bindException, messageSource, locale);

        return HcsResponse.of(hcsException.validation(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage()), errorResults));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public HcsResponse NumberFormatExceptionHandler() {

        ErrorCode error = ErrorCode.NUMBER_FORMAT;

        return HcsResponse.of(hcsException.exception(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage())));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClubAccessDeniedException.class)
    public HcsResponse clubAccessDeniedHandler() {

        ErrorCode error = ErrorCode.CLUB_ACCESS_DENIED;

        return HcsResponse.of(hcsException.exception(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseException.class)
    public HcsResponse databaseExceptionHandler(DatabaseException e) {

        ErrorCode error = ErrorCode.DATABASE_ERROR;

        return HcsResponse.of(hcsException.exceptionAndLocation(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage()), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyJoinedClubException.class)
    public HcsResponse alreadyJoinedExceptionHandler() {

        ErrorCode error = ErrorCode.ALREADY_JOINED_CLUB;

        return HcsResponse.of(hcsException.exception(error.getStatus(), new ExceptionResult(error.getErrorCode(), error.getMessage())));
    }

    // TODO 추후 Response가 만들어지면 공통으로 처리될 error에 대한 전역적인 @ExceptionHandler 추가 작성될 것임.
}
