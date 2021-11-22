create table User (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 id key',
    email VARCHAR(30),
    nickname VARCHAR(10),
    password VARCHAR(15),
    emailVerified BOOLEAN,
    emailCheckToken VARCHAR(50),
    emailCheckTokenGeneratedAt datetime,
    joinedAt datetime,
    age SMALLINT,
    position VARCHAR(10),
    location VARCHAR(20)
)