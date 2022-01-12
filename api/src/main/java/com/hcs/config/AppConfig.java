package com.hcs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hcs.domain.User;
import com.hcs.domain.Club;
import com.hcs.dto.response.user.UserInfoDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(User.class, UserInfoDto.class).addMappings(mapping -> {
            mapping.map(User::getId, UserInfoDto::setUserId);

        modelMapper.typeMap(Club.class, ClubInListDto.class).addMappings(mapping -> {
            mapping.map(Club::getId, ClubInListDto::setClubId);

        });

        modelMapper.typeMap(Club.class, ClubInfoDto.class).addMappings(mapping -> {
            mapping.map(Club::getId, ClubInfoDto::setClubId);
        });

        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

}
