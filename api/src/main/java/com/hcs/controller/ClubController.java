package com.hcs.controller;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final HcsResponseManager responseManager;

    @PostMapping("/submit")
    public HcsResponse createClub(@Valid @RequestBody ClubDto clubDto, HttpServletResponse response) throws IOException {
        //TODO : 로그인한 유저인지 검증 추가

        Club newClub = clubService.saveNewClub(clubDto);
        return responseManager.submit.club(newClub.getId());
    }

    @GetMapping("/info")
    public HcsResponse clubInfo(@RequestParam("clubId") Long id) {
        Club club = clubService.getClub(id);
        return responseManager.info.club(club);
    }

    //TODO : club list
//    @GetMapping("/list")
//    public HcsResponse clubList(@RequestParam("page")int page,@RequestParam("category") String category) {
//        //TODO : managers, members 필드 대신 managerCount, memberCount 필드로 변경
//
//        List<Club> clubList = clubService.getClubsWithPaging(page, category);
//        return  responseManager.clubList(clubList);
//    }

    //TODO : delete club

    //TODO : update club

    //TODO : exception 옮기기
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NumberFormatException.class)
//    public ExceptionResult handleNumberFormatException(NumberFormatException e) {
//        return new ExceptionResult("잘못된 club id 값을 넣었습니다.", e.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ExceptionResult handleIllegalArgException(IllegalArgumentException e) {
//        return new ExceptionResult("존재하지 않는 club id 값입니다.", e.getMessage());
//    }

}
