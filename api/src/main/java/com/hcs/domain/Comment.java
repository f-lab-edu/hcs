package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NamedEntityGraph(name = "Comment.withAuthor", attributeNodes = {
        @NamedAttributeNode(value = "author")}
)

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

    @Column(name = "parentCommentId", insertable = false, updatable = false)
    private Long parentCommentId;

    @Column(name = "tradePostId", insertable = false, updatable = false)
    private Long tradePostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;

    @Column(name = "contents")
    private String contents;

    @Column(name = "registerationTime")
    private LocalDateTime registerationTime;
}
