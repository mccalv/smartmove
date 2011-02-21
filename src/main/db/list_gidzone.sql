DROP TABLE list_gidzone;

CREATE TABLE list_gidzone (
       id INT8 NOT NULL
     , id_list INT8
     , id_gidzone INT8
     , ord DOUBLE PRECISION
     , PRIMARY KEY (id)
     , CONSTRAINT FK_list_menu_3 FOREIGN KEY (id_gidzone)
                  REFERENCES gidZone (id)
     , CONSTRAINT FK_list_gidzone_2 FOREIGN KEY (id_list)
                  REFERENCES list (id) ON DELETE CASCADE ON UPDATE CASCADE
);

