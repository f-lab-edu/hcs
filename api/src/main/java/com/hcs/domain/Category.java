package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Category {
    /*  순서
    sports
    reading
    music
    fashion
    beauty
    art
    crafts
    game
    study
    etc
     */
    private final long id;
    private final String name;
}
