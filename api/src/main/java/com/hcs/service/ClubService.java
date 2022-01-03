package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ModelMapper modelMapper;
    private final ClubMapper clubMapper;
    private final SqlSession sqlSession;
    private final CategoryService categoryService;

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

    public List<Club> getClubListWithPagingAndCategory(int page, int count, long categoryId) {
        RowBounds rowBounds = new RowBounds((page - 1) * count, count);
        return sqlSession.selectList("com.hcs.mapper.ClubMapper.findByPageAndCategory", categoryId, rowBounds);
    }

    public long getAllClubCounts() {
        return clubMapper.countByAllClubs();
    }
}
