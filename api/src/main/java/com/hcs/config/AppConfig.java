package com.hcs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.Club;
import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.response.chatmessage.ChatMessageInfoDto;
import com.hcs.dto.response.chatroom.ChatRoomInfoDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.comment.CommentInfoDto;
import com.hcs.dto.response.tradePost.TradePostInfoDto;
import com.hcs.dto.response.user.ChatUserInfoDto;
import com.hcs.dto.response.user.UserInfoDto;
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
        });

        modelMapper.typeMap(User.class, ChatUserInfoDto.class).addMappings(mapping -> {
            mapping.map(User::getId, ChatUserInfoDto::setUserId);
        });

        modelMapper.typeMap(Club.class, ClubInListDto.class).addMappings(mapping -> {
            mapping.map(Club::getId, ClubInListDto::setClubId);
        });

        modelMapper.typeMap(Club.class, ClubInfoDto.class).addMappings(mapping -> {
            mapping.map(Club::getId, ClubInfoDto::setClubId);
        });

        modelMapper.typeMap(TradePost.class, TradePostInfoDto.class).addMappings(mapping -> {
            mapping.map(TradePost::getId, TradePostInfoDto::setTradePostId);
        });

        modelMapper.typeMap(Comment.class, CommentInfoDto.class).addMappings(mapping -> {
            mapping.map(Comment::getId, CommentInfoDto::setCommentId);
        });

        modelMapper.typeMap(ChatRoom.class, ChatRoomInfoDto.class).addMappings(mapping -> {
            mapping.map(ChatRoom::getId, ChatRoomInfoDto::setChatRoomId);
        });

        modelMapper.typeMap(ChatMessage.class, ChatMessageInfoDto.class).addMappings(mapping -> {
            mapping.map(ChatMessage::getId, ChatMessageInfoDto::setChatMessageId);
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

//    @Bean(name = "mvcHandlerMappingIntrospector")
//    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
//        return new HandlerMappingIntrospector();
//    }
}
