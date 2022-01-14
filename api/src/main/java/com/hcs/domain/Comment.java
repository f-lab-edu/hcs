package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Comment")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "parentCommentId")
    private long parentCommentId;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;

    @Column(name = "contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "tradePostId")
    private TradePost tradePost;

    @OneToMany
    @JoinColumn(name = "parentCommentId")
    private Set<Comment> replys = new HashSet<>();

    @Column(name = "registerationTime")
    private LocalDateTime registerationTime;
}
