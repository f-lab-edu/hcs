package com.hcs.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @EqualsAndHashCode.Include
    private Long id;

    private User author;
    private String content;
    private TradePost tradePost;

    private LocalDateTime registerationTime;


}
