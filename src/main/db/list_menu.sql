DROP TABLE list_menu;

CREATE TABLE list_menu (
       id INT8
     , id_list INT8
     , id_menu INT8
     , order DOUBLE PRECISION
     , CONSTRAINT FK_list_menu_1 FOREIGN KEY ()
                  REFERENCES menu ()
     , CONSTRAINT FK_list_menu_2 FOREIGN KEY ()
                  REFERENCES list ()
);

