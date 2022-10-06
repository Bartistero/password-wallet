CREATE TABLE users
(
    id                   INTEGER NOT NULL AUTO_INCREMENT,
    login                VARCHAR(30),
    password_hash        VARCHAR(30),
    salt                 VARCHAR(20),
    isPasswordKeptAsHash BOOLEAN,
    PRIMARY KEY (id)
);

Create Table password
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    password    VARCHAR(256),
    web_address VARCHAR(256),
    description VARCHAR(256),
    login       VARCHAR(30),
    id_user     INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_user) REFERENCES users(id)
);