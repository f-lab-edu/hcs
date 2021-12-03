package com.hcs.domain;

import lombok.*;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TradePost {

    @EqualsAndHashCode.Include
    private Long id;

    private Long number;
    private User author;
    private String title;
    private String productStatus;
    private String category;
    private String description;
    @Lob
    private byte[] pictures;
    private String appointmentLocation;
    private Integer price;
    private Integer views;
    private List<Comment> comments = new ArrayList<>();

    private boolean salesStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerationTime;
}
