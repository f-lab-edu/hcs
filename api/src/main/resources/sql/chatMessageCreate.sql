create table ChatMessage
(
    id        int AUTO_INCREMENT PRIMARY KEY COMMENT '채팅 메시지 id key',
    roomId    VARCHAR(255) NOT NULL,
    authorId  int          NOT NULL,
    message   VARCHAR(255) NOT NULL,
    createdAt datetime,

    FOREIGN KEY (roomId) REFERENCES ChatRoom (id) ON DELETE CASCADE,
    FOREIGN KEY (authorId) REFERENCES User (id) ON DELETE CASCADE
)
