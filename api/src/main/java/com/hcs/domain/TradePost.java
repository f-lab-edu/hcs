package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TradePost {

    @EqualsAndHashCode.Include
    private long id;

    private User author;
    private String title;
    private String productStatus;
    private String category;
    private String description;
    @Lob
    private byte[] pictures;
    private String locationName;
    private double lat;
    private double lng;
    private int price;
    private int views;

    private Set<Comment> comments = new HashSet<>();

    private boolean salesStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerationTime;
}
