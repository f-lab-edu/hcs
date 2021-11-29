package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ModelMapper modelMapper;

    private final ClubMapper clubMapper;

    public Club saveNewClub(@Valid ClubDto clubDto) {
        Club club = modelMapper.map(clubDto, Club.class);
        try {
            save(club);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return club;
    }



    public void save(Club club) { clubMapper.save(club); }
    public Club getClub(Long id) {
        Club club = clubMapper.findById(id);
        checkExistingClub(club);
        return club;
    }

    private void checkExistingClub(Club club) {
        if(club==null){
            throw new IllegalArgumentException();
        }
    }

}
