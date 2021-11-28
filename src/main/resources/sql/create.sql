create table User
(
    id                         int AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 id key',
    email                      VARCHAR(30)  NOT NULL,
    nickname                   VARCHAR(10)  NOT NULL,
    password                   VARCHAR(200) NOT NULL,
    emailVerified              BOOLEAN,
    emailCheckToken            VARCHAR(50),
    emailCheckTokenGeneratedAt datetime,
    joinedAt                   datetime,
    age                        SMALLINT,
    position                   VARCHAR(10),
    location                   VARCHAR(20)
)

create table Club
(
    id                         int AUTO_INCREMENT PRIMARY KEY COMMENT '동호회 id key',
    title                      VARCHAR(20) NOT NULL,
    description                VARCHAR(50),
    created_at                 datetime NOT NULL,
    location                   VARCHAR(20) NOT NULL,
    category                   VARCHAR(20) NOT NULL
)

