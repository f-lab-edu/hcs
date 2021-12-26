create table ChatRoom
(
    id        int AUTO_INCREMENT PRIMARY KEY COMMENT '채팅방 id key',
    roomId    VARCHAR(255) NOT NULL,
    createdAt datetime
)
