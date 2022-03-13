create table ChatRoom
(
    id             VARCHAR(255) PRIMARY KEY COMMENT '채팅방 id key',
    lastChatMesgId int,
    createdAt      datetime,

    FOREIGN KEY (lastChatMesgId) REFERENCES ChatMessage (id) ON DELETE CASCADE
)
