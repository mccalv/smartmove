DROP TABLE geosearch;

CREATE TABLE geosearch (
       query VARCHAR(255) NOT NULL
     , date CHAR(10)
     , value TEXT
     , locale CHAR(2)
     , PRIMARY KEY (query)
);

