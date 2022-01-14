package com.hcs.controller;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsModify;
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
    private final HcsModify hcsUpdate;
    private final CategoryService categoryService;

    @PostMapping("/submit")
    public HcsResponse createClub(@Valid @RequestBody ClubSubmitDto clubDto) {
        //TODO : 로그인한 유저인지 검증 추가

        Club newClub = clubService.saveNewClub(clubDto);
        return responseManager.makeHcsResponse(submit.club(newClub.getId()));
    }

    @GetMapping("/info")
    public HcsResponse clubInfo(@RequestParam("clubId") long id) {
        ClubInfoDto clubInfoDto = clubService.getClubInfo(id);
        return responseManager.makeHcsResponse(info.club(clubInfoDto));

    }

    @GetMapping("/list")
    public HcsResponse clubList(@RequestParam("page") int page, @RequestParam("category") String category) {
        int count = 10;
        long categoryId = categoryService.getCategoryId(category);
        List<ClubInListDto> clubInListDtos = clubService.getClubListWithPagingAndCategory(page, count, categoryId);
        long allClubCounts = clubService.getAllClubCounts();
        return responseManager.makeHcsResponse(hcsList.club(clubInListDtos, page, count, allClubCounts));
    }

    @PostMapping("/modify")
    public HcsResponse modifyClub(@RequestBody ClubSubmitDto clubDto, @RequestParam("clubId") long clubId) {
        clubId = clubService.modifyClub(clubId, clubDto);
        String clubUrl = clubService.makeClubUrl(clubId);
        return responseManager.makeHcsResponse(hcsUpdate.club(clubId, clubUrl));
    }

    //TODO : update club

}
