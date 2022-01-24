package com.hcs.controller;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.club.ClubExpulsionDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.club.ClubJoinDto;
import com.hcs.dto.response.club.ClubResignDto;
import com.hcs.dto.response.method.HcsDelete;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsModify;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.CategoryService;
import com.hcs.service.ClubService;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final HcsInfo info;
    private final HcsSubmit submit;
    private final HcsList hcsList;
    private final HcsDelete delete;
    private final HcsModify hcsUpdate;
    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping("")
    public HcsResponse createClub(@Valid @RequestBody ClubSubmitDto clubDto, @RequestParam("userEmail") String userEmail) {
        //TODO : 로그인한 유저인지 검증 추가
        User manager = userService.findByEmail(userEmail);
        Club newClub = clubService.saveNewClub(clubDto, manager.getId());
        return HcsResponse.of(submit.club(newClub.getId()));
    }

    @GetMapping("")
    public HcsResponse clubInfo(@RequestParam("clubId") long id) {
        ClubInfoDto clubInfoDto = clubService.getClubInfo(id);
        return HcsResponse.of(info.club(clubInfoDto));
    }

    @GetMapping("/list")
    public HcsResponse clubList(@RequestParam("page") int page, @RequestParam("category") String category) {
        int count = 10;
        long categoryId = categoryService.getCategoryId(category);
        List<ClubInListDto> clubInListDtos = clubService.getClubListWithPagingAndCategory(page, count, categoryId);
        long allClubCounts = clubService.getAllClubCounts();
        return HcsResponse.of(hcsList.club(clubInListDtos, page, count, allClubCounts));
    }

    @PutMapping("")
    public HcsResponse modifyClub(@RequestBody ClubSubmitDto clubDto, @RequestParam("clubId") long clubId) {
        clubId = clubService.modifyClub(clubId, clubDto);
        String clubUrl = clubService.makeClubUrl(clubId);
        return HcsResponse.of(hcsUpdate.club(clubId, clubUrl));
    }

    @DeleteMapping("")
    public HcsResponse deleteClub(@RequestParam("clubId") long clubId, @RequestParam("userEmail") String userEmail) {
        //TODO : 보안 설정 후 userEmail 변경
        User user = userService.findByEmail(userEmail);
        clubService.deleteClub(clubId, user.getId());
        return HcsResponse.of(delete.club(clubId));
    }

    @PostMapping("/member")
    public HcsResponse joinClubAsMember(@RequestParam("clubId") long clubId, @RequestParam("userEmail") String userEmail) {
        //TODO : 보안 설정 후 userEmail 변경
        User user = userService.findByEmail(userEmail);
        ClubJoinDto clubJoinDto = clubService.joinClub(clubId, user);
        return HcsResponse.of(submit.joinClub(clubJoinDto));
    }

    @DeleteMapping("/member/expulsion")
    public HcsResponse expulsionMember(@RequestParam("clubId") long clubId, @RequestParam("managerEmail") String managerEmail, @RequestParam("userId") long userId) {
        //TODO : 보안 설정 후 userEmail 변경
        Club club = clubService.expulsionMember(clubId, managerEmail, userId);
        ClubExpulsionDto dto = new ClubExpulsionDto(userId, club.getMemberCount());
        return HcsResponse.of(delete.expulsionMember(dto));
    }

    @DeleteMapping("/member/resign")
    public HcsResponse resignMember(@RequestParam("clubId") long clubId, @RequestParam("userEmail") String userEmail) {
        //TODO : 보안 설정 후 userEmail 변경
        User user = userService.findByEmail(userEmail);
        Club club = clubService.resignMember(clubId, user.getId());
        ClubResignDto dto = new ClubResignDto(user.getId(), club.getMemberCount());
        return HcsResponse.of(delete.resignMember(dto));
    }

}
