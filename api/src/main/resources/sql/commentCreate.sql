create table Comment
(
    id                int AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 id key',
    authorId          int          NOT NULL,
    contents          VARCHAR(200) NOT NULL,
    tradePostId       int          NOT NULL,
    registerationTime datetime,

    FOREIGN KEY (authorId) REFERENCES User (id) ON DELETE CASCADE,
    FOREIGN KEY (tradePostId) REFERENCES TradePost (id) ON DELETE CASCADE
)
