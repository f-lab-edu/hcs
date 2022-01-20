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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NamedEntityGraph(name = "TradePost.withAuthor", attributeNodes = {
        @NamedAttributeNode(value = "author")}
)

@Data
@Entity
@Table(name = "TradePost")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TradePost {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;

    @Column(name = "productStatus")
    private String productStatus;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "pictures")
    private byte[] pictures;

    @Column(name = "locationName")
    private String locationName;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "price")
    private Integer price;

    @Column(name = "views")
    private Integer views;

    @Column(name = "salesStatus")
    private Boolean salesStatus;

    @Column(name = "registerationTime")
    private LocalDateTime registerationTime;
}
