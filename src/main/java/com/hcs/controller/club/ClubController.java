package com.hcs.controller.club;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.service.ClubService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/submit")
    public String createClubForm() {

        //TODO : 로그인한 유저인지 검증 추가

        return "createClubForm page";
    }

    @PostMapping("/submit")
    public void createClub(@Valid @RequestBody ClubDto clubDto, HttpServletResponse response) throws IOException {
        Club newClub = clubService.saveNewClub(clubDto); //TODO : 로그인한 유저인지 검증 추가
        response.sendRedirect("/club/" + newClub.getId());
    }

    @GetMapping("/{id}")
    public Club viewClub(@PathVariable String id) {
        return clubService.getClub(Long.parseLong(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentExHandle(IllegalArgumentException e) {
        return e.getMessage();
    }

}
