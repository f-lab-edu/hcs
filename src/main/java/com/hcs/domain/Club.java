package com.hcs.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Club {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private User manager;

    @ManyToMany
    private Set<User> members = new HashSet<>();

    private LocalDateTime publishedDateTime;
    private LocalDateTime closedDateTime;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

}
