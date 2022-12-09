CREATE TABLE users
(
    id       bigint not null,
    username VARCHAR(30),
    password VARCHAR(256),
    salt     VARCHAR(20),
    type     VARCHAR(15),
    attempt  int,
    block    boolean,
    PRIMARY KEY (id)
);

Create Table passwords
(
    id          bigint     not null,
    password    VARCHAR(256),
    web_address VARCHAR(256),
    description VARCHAR(256),
    login       VARCHAR(30),
    idUser      VARCHAR(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users (id)
);

Create Table login_history
(
    id     bigint     not null,
    data   date,
    type   varchar(30),
    ip     varchar(250),
    idUser VARCHAR(4) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES users (id)
);

Create Table block_ip_address
(
    id      bigint not null,
    ip      varchar(250),
    attempt int,
    blocked boolean,
    data    date,
    PRIMARY KEY (id)
)