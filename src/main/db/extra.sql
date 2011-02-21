
DROP TABLE extra;

CREATE TABLE extra (
       id INT8 NOT NULL
     , item_id INT8
     , label VARCHAR(255)
     , value TEXT
     , locale CHAR(4)
     , PRIMARY KEY (id)
);

ALTER TABLE extra
  ADD CONSTRAINT FK_extra_1
      FOREIGN KEY (item_id)
      REFERENCES item (id)
   ON DELETE CASCADE
   ON UPDATE CASCADE;

