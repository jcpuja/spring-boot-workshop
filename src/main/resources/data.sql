DROP TABLE IF EXISTS greetings;

CREATE TABLE greetings
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    language VARCHAR(10)  NOT NULL,
    text     VARCHAR(250) NOT NULL
);

INSERT INTO greetings (language, text)
VALUES ('en', 'Hello World!'),
       ('de', 'Hallo Welt!'),
       ('fr', 'Salut, monde !');
