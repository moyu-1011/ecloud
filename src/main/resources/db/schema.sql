CREATE DATABASE IF NOT EXISTS ecloud;
DROP TABLE IF EXISTS  object_type;
CREATE TABLE object_type(
    id INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR (30) UNIQUE
)DEFAULT CHARACTER SET = utf8;