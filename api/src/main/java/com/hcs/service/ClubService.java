package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.CategoryMapper;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ModelMapper modelMapper;
    private final ClubMapper clubMapper;
    private final CategoryService categoryService;

    public Club saveNewClub(@Valid ClubDto clubDto) {
        Club club = modelMapper.map(clubDto, Club.class);
        try {
            clubMapper.insertClub(club);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return club;
    }

    public Club getClub(Long id) {
        Club club = clubMapper.findById(id);
        checkExistingClub(club);
        return club;
    }

    private void checkExistingClub(Club club) {
        if (club == null) {
            throw new IllegalArgumentException(); //TODO : exception 만들어서 교체하기
        }
    }

}
