create table Club
(
    id                         int AUTO_INCREMENT PRIMARY KEY COMMENT '동호회 id key',
    title                      VARCHAR(30) NOT NULL,
    description                VARCHAR(50),
    createdAt                  datetime NOT NULL,
    location                   VARCHAR(20) NOT NULL,
    category                   VARCHAR(20) NOT NULL
    #managerCount               int DEFAULT 0,
    #memberCount                int DEFAULT 0 성능 개선시 사용 예정
)
