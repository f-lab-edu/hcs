package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.exception.club.ClubAccessDeniedException;
import com.hcs.exception.global.DatabaseException;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${domain.url}")
    private String domainUrl;

    public Club saveNewClub(@Valid ClubSubmitDto clubDto, long userId) {
        Club club = modelMapper.map(clubDto, Club.class);
        club.setCategoryId(categoryService.getCategoryId(clubDto.getCategory()));
        club.setCreatedAt(LocalDateTime.now());
        club.setManagerCount(1);

        int insertClubResult = clubMapper.insertClub(club);
        if (insertClubResult != 1) {
            throw new DatabaseException("DB club insert");
        }
        int insertManagerResult = clubMapper.joinManagerById(club.getId(), userId);
        if (insertManagerResult != 1) {
            throw new DatabaseException("DB club manager insert");
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
            throw new IllegalArgumentException(); //TODO : exception 만들어서 교체하기
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
        return domainUrl + "club/" + clubId;
    }

    public long deleteClub(long clubId, long managerId) {
        int result = clubMapper.deleteClub(clubId, managerId);
        if (result != 1) {
            throw new ClubAccessDeniedException();
        }
        return clubId;
    }
}
