package com.hcs.controller;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @ExceptionHandler : 컨트롤러 내에서 특정 예외가 발생했을때 실행될 메소드 위에 사용한다.
 */

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    private final MessageSource messageSource;

    @GetMapping("/submit")
    public ResponseEntity<?> createClubForm() {
        //TODO : 로그인한 유저인지 검증 추가

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> createClub(@Valid @RequestBody ClubDto clubDto, HttpServletResponse response) throws IOException {
        Club newClub = clubService.saveNewClub(clubDto); //TODO : 로그인한 유저인지 검증 추가
        return new ResponseEntity<>(newClub, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Club viewClub(@PathVariable String id) {
        return clubService.getClub(Long.parseLong(id));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NumberFormatException.class)
    public ExceptionResult handleNumberFormatException(NumberFormatException e) {
        return new ExceptionResult("잘못된 club id 값을 넣었습니다.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResult handleIllegalArgException(IllegalArgumentException e) {
        return new ExceptionResult("존재하지 않는 club id 값입니다.", e.getMessage());
    }

}
