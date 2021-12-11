package com.hcs.domain;

import lombok.*;

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
public class Comment {

    @EqualsAndHashCode.Include
    private Long id;

    private User author;
    private String contents;
    private TradePost tradePost;
    private Set<Comment> replys = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerationTime;

}
