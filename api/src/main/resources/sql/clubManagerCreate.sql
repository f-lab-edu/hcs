create table club_managers
(
    clubId              int NOT NULL,
    managerId           int NOT NULL,

    PRIMARY KEY (clubId, managerId),
    FOREIGN KEY (clubId) REFERENCES Club (id),
    FOREIGN KEY (managerId) REFERENCES User (id)
)
