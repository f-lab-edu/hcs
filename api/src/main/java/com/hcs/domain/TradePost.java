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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
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
    private double lng;

    @Column(name = "lat")
    private double lat;

    @Column(name = "price")
    private int price;

    @Column(name = "views")
    private int views;

    @OneToMany(mappedBy = "tradePost")
    private Set<Comment> comments = new HashSet<>();

    @Column(name = "salesStatus")
    private boolean salesStatus;

    @Column(name = "registerationTime")
    private LocalDateTime registerationTime;
}
