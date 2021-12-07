create table TradePost
(
    id                  int AUTO_INCREMENT PRIMARY KEY COMMENT '중고거래 게시글 id key',
    number              int          NOT NULL,
    authorId            int          NOT NULL,
    title               VARCHAR(30)  NOT NULL,
    productStatus       VARCHAR(10)  NOT NULL,
    category            VARCHAR(15)  NOT NULL,
    description         VARCHAR(250) NOT NULL,
    pictures            blob,
    appointmentLocation VARCHAR(250),
    price               int          NOT NULL,
    salesStatus         boolean      NOT NULL,
    registerationTime   datetime,

    FOREIGN KEY (authorId) REFERENCES User (id) ON DELETE CASCADE
)
