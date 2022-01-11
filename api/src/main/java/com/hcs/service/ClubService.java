package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    public Club saveNewClub(@Valid ClubDto clubDto) {
        Club club = modelMapper.map(clubDto, Club.class);
        club.setCategoryId(categoryService.getCategoryId(clubDto.getCategory()));
        try {
            clubMapper.insertClub(club);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            dto.setClubUrl(domainUrl + "club/" + dto.getClubId());
            clubInListDtos.add(dto);
        }
        return clubInListDtos;
    }

    public long getAllClubCounts() {
        return clubMapper.countByAllClubs();
    }
}
