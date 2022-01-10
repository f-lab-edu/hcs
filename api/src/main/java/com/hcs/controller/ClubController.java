package com.hcs.controller;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.CategoryService;
import com.hcs.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @ExceptionHandler : 컨트롤러 내에서 특정 예외가 발생했을때 실행될 메소드 위에 사용한다.
 */

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final HcsResponseManager responseManager;
    private final HcsInfo info;
    private final HcsSubmit submit;
    private final HcsList hcsList;
    private final CategoryService categoryService;

    @PostMapping("/submit")
    public HcsResponse createClub(@Valid @RequestBody ClubDto clubDto) {
        //TODO : 로그인한 유저인지 검증 추가

        Club newClub = clubService.saveNewClub(clubDto);
        return responseManager.makeHcsResponse(submit.club(newClub.getId()));
    }

    @GetMapping("/info")
    public HcsResponse clubInfo(@RequestParam("clubId") long id) {
        Club club = clubService.getClub(id);
        String category = categoryService.getCategoryName(club.getCategoryId());
        return responseManager.makeHcsResponse(info.club(club, category));

    }

    @GetMapping("/list")
    public HcsResponse clubList(@RequestParam("page") int page, @RequestParam("category") String category) {
        //TODO : managers, members 필드 대신 managerCount, memberCount 필드로 변경

        int count = 10;
        long categoryId = categoryService.getCategoryId(category);
        List<Club> clubList = clubService.getClubListWithPagingAndCategory(page, count, categoryId);
        long allClubCounts = clubService.getAllClubCounts();
        return responseManager.makeHcsResponse(hcsList.club(clubList, category, page, count, allClubCounts));
    }

    //TODO : delete club

    //TODO : update club

}
