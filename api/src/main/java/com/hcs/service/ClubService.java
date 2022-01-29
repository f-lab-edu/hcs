package com.hcs.service;

import com.hcs.config.DomainUrlConfig;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.club.ClubJoinDto;
import com.hcs.exception.club.AlreadyJoinedClubAsManagerException;
import com.hcs.exception.club.AlreadyJoinedClubException;
import com.hcs.exception.club.ClubAccessDeniedException;
import com.hcs.exception.club.NotJoinedClubException;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ModelMapper modelMapper;
    private final ClubMapper clubMapper;
    private final SqlSession sqlSession;
    private final CategoryService categoryService;
    private final DomainUrlConfig domainUrlConfig;
    private final UserService userService;

    public Club saveNewClub(@Valid ClubSubmitDto clubDto, long userId) {
        Club club = modelMapper.map(clubDto, Club.class);
        club.setCategoryId(categoryService.getCategoryId(clubDto.getCategory()));
        club.setCreatedAt(LocalDateTime.now());
        club.setManagerCount(1);

        int insertClubResult = clubMapper.insertClub(club);
        if (insertClubResult != 1) {
            throw new RuntimeException("DB error");
        }
        int insertManagerResult = clubMapper.joinManagerById(club.getId(), userId);
        if (insertManagerResult != 1) {
            throw new RuntimeException("DB error");
        }
        return club;
    }

    public Club getClub(long id) {
        Club club = clubMapper.findById(id);
        checkExistingClub(club);
        return club;
    }

    private void checkExistingClub(Club club) {
        if (club == null) {
            throw new IllegalArgumentException("club");
        }
    }

    public List<ClubInListDto> getClubListWithPagingAndCategory(int page, int count, long categoryId) {
        RowBounds rowBounds = new RowBounds((page - 1) * count, count);
        List<Club> clubList = sqlSession.selectList("com.hcs.mapper.ClubMapper.findByPageAndCategory", categoryId, rowBounds);
        List<ClubInListDto> clubInListDtos = new ArrayList<>();
        for (Club c : clubList) {
            ClubInListDto dto = modelMapper.map(c, ClubInListDto.class);
            dto.setClubUrl(makeClubUrl(dto.getClubId()));
            dto.setCategory(categoryService.getCategoryName(c.getCategoryId()));
            clubInListDtos.add(dto);
        }
        return clubInListDtos;
    }

    public long getAllClubCounts() {
        return clubMapper.countByAllClubs();
    }

    public ClubInfoDto getClubInfo(long id) {
        Club club = getClub(id);
        ClubInfoDto dto = modelMapper.map(club, ClubInfoDto.class);
        dto.setCategory(categoryService.getCategoryName(club.getCategoryId()));
        dto.setClubUrl(makeClubUrl(club.getId()));
        return dto;
    }

    public long modifyClub(long clubId, ClubSubmitDto clubDto) {
        Club club = getClub(clubId);
        club = modelMapper.map(clubDto, Club.class);
        club.setCategoryId(categoryService.getCategoryId(clubDto.getCategory()));
        club.setId(clubId);
        clubMapper.updateClub(club);
        return club.getId();
    }

    public String makeClubUrl(long clubId) {
        return domainUrlConfig.getUrl() + "club/" + clubId;
    }

    public long deleteClub(long clubId, long managerId) {
        int result = clubMapper.deleteClub(clubId, managerId);
        if (result != 1) {
            throw new ClubAccessDeniedException();
        }
        return clubId;
    }

    public ClubJoinDto joinClub(long clubId, User user) {
        Club club = getClub(clubId);
        checkAlreadyJoinedClub(club, user);
        int result = clubMapper.joinMemberById(club.getId(), user.getId());
        if (result != 1) {
            throw new RuntimeException("DB error");
        }
        plusMemberCount(club);
        ClubJoinDto dto = new ClubJoinDto(user.getId(), club.getMemberCount());
        return dto;
    }

    public void checkAlreadyJoinedClub(Club club, User user) {
        if (clubMapper.checkClubManager(club.getId(), user.getId()) ||
                clubMapper.checkClubMember(club.getId(), user.getId())) {
            throw new AlreadyJoinedClubException();
        }
    }

    public void plusMemberCount(Club club) {
        club.setMemberCount(club.getMemberCount() + 1);
        int result = clubMapper.updateMemberCount(club.getId(), club.getMemberCount());
        if (result != 1) {
            throw new RuntimeException("DB error");
        }
    }

    public Club expulsionMember(long clubId, String managerEmail, long userId) {
        Club club = getClub(clubId);
        User manager = userService.findByEmail(managerEmail);
        User member = userService.findById(userId);

        boolean managerExist = clubMapper.checkClubManager(club.getId(), manager.getId());
        if (!managerExist) {
            throw new ClubAccessDeniedException();
        }

        boolean memberExist = clubMapper.checkClubMember(club.getId(), member.getId());
        if (!memberExist) {
            boolean memberIsManager = clubMapper.checkClubManager(club.getId(), member.getId());
            if (memberIsManager) {
                // TODO : manager 삭제 요청
            } else {
                throw new NotJoinedClubException();
            }

        }

        int deleteMemberResult = clubMapper.deleteMember(club.getId(), member.getId());
        if (deleteMemberResult != 1) {
            throw new RuntimeException("DB error");
        }

        club.setMemberCount(club.getMemberCount() - 1);
        int updateMemberCountResult = clubMapper.updateMemberCount(club.getId(), club.getMemberCount());
        if (updateMemberCountResult != 1) {
            throw new RuntimeException("DB error");
        }
        return club;
    }

    public Club resignMember(long clubId, long memberId) {
        Club club = getClub(clubId);
        if (!clubMapper.checkClubMember(club.getId(), memberId)) {
            throw new NotJoinedClubException();
        }
        int deleteMemberResult = clubMapper.deleteMember(club.getId(), memberId);
        if (deleteMemberResult != 1) {
            throw new RuntimeException("DB error");
        }
        club.setMemberCount(club.getMemberCount() - 1);
        int updateMemberCountResult = clubMapper.updateMemberCount(club.getId(), club.getMemberCount());
        if (updateMemberCountResult != 1) {
            throw new RuntimeException("DB error");
        }
        return club;
    }

    public Club makeManager(long clubId, String managerEmail, long userId) {
        Club club = getClub(clubId);
        User manager = userService.findByEmail(managerEmail);
        User member = userService.findById(userId);

        if (!clubMapper.checkClubManager(club.getId(), manager.getId())) {
            throw new ClubAccessDeniedException();
        }

        if (clubMapper.checkClubManager(club.getId(), member.getId())) {
            throw new AlreadyJoinedClubAsManagerException();
        }

        if (!clubMapper.checkClubMember(club.getId(), member.getId())) {
            throw new NotJoinedClubException();
        }

        int deleteMember = clubMapper.deleteMember(club.getId(), member.getId());
        if (deleteMember != 1) {
            throw new RuntimeException("DB error");
        }
        club.setMemberCount(club.getMemberCount() - 1);
        int updateMemberCount = clubMapper.updateMemberCount(club.getId(), club.getMemberCount());
        if (updateMemberCount != 1) {
            throw new RuntimeException("DB error");
        }

        int insertManager = clubMapper.joinManagerById(club.getId(), member.getId());
        if (insertManager != 1) {
            throw new RuntimeException("DB error");
        }
        club.setManagerCount(club.getManagerCount() + 1);
        int updateManagerCount = clubMapper.updateManagerCount(club.getId(), club.getManagerCount());
        if (updateManagerCount != 1) {
            throw new RuntimeException("DB error");
        }

        return club;
    }
}
