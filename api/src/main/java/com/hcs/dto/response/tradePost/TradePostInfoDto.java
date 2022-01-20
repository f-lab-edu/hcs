package com.hcs.dto.response.tradePost;

import com.hcs.dto.response.user.UserInfoDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TradePostInfoDto {

    private long tradePostId;
    private String title;

    private UserInfoDto author;
    private String productStatus;
    private String category;
    private String description;

    // TODO : 클래스 파일 생성 및 사진에 대한 비즈니스 로직 작성
//    private Set<Picture> pictures;

    private String locationName;
    private double lng;
    private double lat;
    private int price;
    private int views;

    private boolean salesStatus;
    private LocalDateTime registerationTime;
}

