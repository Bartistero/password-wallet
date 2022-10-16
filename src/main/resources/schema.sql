CREATE TABLE users
(
    id                   bigint not null,
    username                VARCHAR(30),
    password       VARCHAR(30),
    salt                 VARCHAR(20),
    PRIMARY KEY (id)
);

Create Table password
(
    id          bigint not null,
    password    VARCHAR(256),
    web_address VARCHAR(256),
    description VARCHAR(256),
    login       VARCHAR(30),
    id_user     VARCHAR(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users (id)
);