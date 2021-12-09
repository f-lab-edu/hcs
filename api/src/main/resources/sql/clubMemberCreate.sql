create table club_members
(
    clubId              int NOT NULL,
    memberId            int NOT NULL,

    PRIMARY KEY (clubId, memberId),
    FOREIGN KEY (clubId) REFERENCES Club (id),
    FOREIGN KEY (memberId) REFERENCES User (id)
)
