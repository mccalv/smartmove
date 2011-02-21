DROP TABLE item;

CREATE TABLE item (
       id SERIAL NOT NULL
     , title TEXT
     , description TEXT
     , address VARCHAR(255)
     , website VARCHAR(255)
     , telephone VARCHAR(255)
     , email VARCHAR(255)
     , PRIMARY KEY (id)
);

