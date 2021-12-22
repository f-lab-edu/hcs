create table club_members
(
    clubId              int NOT NULL,
    memberId            int NOT NULL,

    PRIMARY KEY (clubId, memberId),
    FOREIGN KEY (clubId) REFERENCES Club (id) on delete cascade,
    FOREIGN KEY (memberId) REFERENCES User (id) on delete cascade
)
