create table ChatRoom_members
(
    chatRoomId VARCHAR(255) NOT NULL,
    memberId   int          NOT NULL,

    PRIMARY KEY (chatRoomId, memberId),
    FOREIGN KEY (chatRoomId) REFERENCES ChatRoom (id) ON DELETE CASCADE,
    FOREIGN KEY (memberId) REFERENCES User (id) ON DELETE CASCADE
)
