create table Category
(
    id      int AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 id key',
    name    VARCHAR(15) NOT NULL
)

insert into Category (name) value ("sports");
insert into Category (name) value ("reading");
insert into Category (name) value ("music");
insert into Category (name) value ("fashion");
insert into Category (name) value ("beauty");
insert into Category (name) value ("art");
insert into Category (name) value ("crafts");
insert into Category (name) value ("game");
insert into Category (name) value ("study");
insert into Category (name) value ("etc");

