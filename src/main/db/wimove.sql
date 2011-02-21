DROP TABLE gps_position;
DROP TABLE item_tag;
DROP TABLE admin_register_log;
DROP TABLE roles_users;
DROP TABLE localizedlist;
DROP TABLE list_gidzone;
DROP TABLE list_item;
DROP TABLE extra;
DROP TABLE time_occurrence;
DROP TABLE localizedItem;
DROP TABLE cost;
DROP TABLE contact;
DROP TABLE item;
DROP TABLE list;
DROP TABLE tag;
DROP TABLE apikey;
DROP TABLE roles;
DROP TABLE users;
DROP TABLE gidZone;
DROP TABLE category;
DROP TABLE gid;

CREATE TABLE gid (
       identifier CHAR(15) NOT NULL
     , name VARCHAR(255)
     , PRIMARY KEY (identifier)
);

CREATE TABLE category (
       category VARCHAR(255) NOT NULL
     , description TEXT
     , PRIMARY KEY (category)
);

CREATE TABLE gidZone (
       id SERIAL8 NOT NULL
     , identifier VARCHAR(255)
     , PRIMARY KEY (id)
);

CREATE TABLE users (
       id_users INT8 NOT NULL
     , name VARCHAR(255)
     , surname VARCHAR(255)
     , email VARCHAR(255)
     , nick VARCHAR(255)
     , password VARCHAR(255)
     , date_activation TIMESTAMP
     , last_login TIMESTAMP
     , enable SMALLINT
     , PRIMARY KEY (id_users)
);

CREATE TABLE roles (
       id_roles INT8 NOT NULL
     , code_role VARCHAR(255)
     , PRIMARY KEY (id_roles)
);

CREATE TABLE apikey (
       hash VARCHAR(255)
     , domain VARCHAR(255)
);

CREATE TABLE tag (
       tag VARCHAR(255) NOT NULL
     , parent_tag VARCHAR(255)
     , description VARCHAR(255)
     , PRIMARY KEY (tag)
     , CONSTRAINT FK_tag_1 FOREIGN KEY (parent_tag)
                  REFERENCES tag (tag)
);

CREATE TABLE list (
       id INT8 NOT NULL
     , name VARCHAR
     , creation_date TIMESTAMP
     , update_date TIMESTAMP
     , id_users INT8
     , PRIMARY KEY (id)
     , CONSTRAINT FK_list_1 FOREIGN KEY (id_users)
                  REFERENCES users (id_users)
);

CREATE TABLE item (
       id INT8 NOT NULL
     , item_id VARCHAR(60)
     , gid_identifier CHAR(15) NOT NULL
     , category VARCHAR(255)
     , website VARCHAR(255)
     , telephone VARCHAR(255)
     , email VARCHAR(255)
     , creation_date TIMESTAMP
     , update_date TIMESTAMP
     , PRIMARY KEY (id)
     , CONSTRAINT FK_item_1 FOREIGN KEY (gid_identifier)
                  REFERENCES gid (identifier)
     , CONSTRAINT FK_item_2 FOREIGN KEY (category)
                  REFERENCES category (category)
);

CREATE TABLE contact (
       id INT8 NOT NULL
     , item_id INT8
     , label VARCHAR(255)
     , value TEXT
     , locale CHAR(2)
     , PRIMARY KEY (id)
     , CONSTRAINT FK_contact_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cost (
       id INT8 NOT NULL
     , item_id INT8
     , label VARCHAR(255)
     , value VARCHAR(255)
     , locale CHAR(2)
     , PRIMARY KEY (id)
     , CONSTRAINT FK_cost_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE localizedItem (
       id INT8 NOT NULL
     , item_id BIGINT
     , locale VARCHAR(4)
     , label VARCHAR(50)
     , value TEXT
     , PRIMARY KEY (id)
     , CONSTRAINT FK_localizedItem_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE time_occurrence (
       id INT8
     , item_id INT8
     , start_date TIMESTAMP
     , end_date TIMESTAMP
     , address VARCHAR(255)
     , CONSTRAINT FK_time_occurrence_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE extra (
       id INT8 NOT NULL
     , item_id INT8
     , label VARCHAR(255)
     , value TEXT
     , locale CHAR(2)
     , PRIMARY KEY (id)
     , CONSTRAINT FK_extra_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE list_item (
       id INT8 NOT NULL
     , id_list INT8
     , id_item INT8
     , ord DOUBLE PRECISION
     , PRIMARY KEY (id)
     , CONSTRAINT FK_list_item_1 FOREIGN KEY (id_list)
                  REFERENCES list (id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_list_item_2 FOREIGN KEY (id_item)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

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

CREATE TABLE localizedlist (
       id INT8 NOT NULL
     , list_id BIGINT
     , locale VARCHAR(4)
     , label VARCHAR(50)
     , value TEXT
     , PRIMARY KEY (id)
     , CONSTRAINT FK_localizedlist_1 FOREIGN KEY (list_id)
                  REFERENCES list (id)
);

CREATE TABLE roles_users (
       id_roles_users INT8
     , id_roles INT8
     , id_users INT8
     , CONSTRAINT FK_roles_users_1 FOREIGN KEY (id_users)
                  REFERENCES users (id_users)
     , CONSTRAINT FK_roles_users_2 FOREIGN KEY (id_roles)
                  REFERENCES roles (id_roles)
);

CREATE TABLE admin_register_log (
       id_admin_register_log INT8 NOT NULL
     , id_users BIGINT
     , register_log_datetime TIMESTAMP
     , PRIMARY KEY (id_admin_register_log)
     , CONSTRAINT FK_admin_register_log_1 FOREIGN KEY (id_users)
                  REFERENCES users (id_users)
);

CREATE TABLE item_tag (
       item_id INT8 NOT NULL
     , tag VARCHAR(255) NOT NULL
     , CONSTRAINT FK_item_tag_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_item_tag_2 FOREIGN KEY (tag)
                  REFERENCES tag (tag)
);

CREATE TABLE gps_position (
       id INT8 NOT NULL
     , item_id INT8
     , latitude FLOAT4
     , longitude FLOAT4
     , address VARCHAR(255)
     , locality VARCHAR(255)
     , PRIMARY KEY (id)
     , CONSTRAINT FK_gps_position_1 FOREIGN KEY (item_id)
                  REFERENCES item (id) ON DELETE CASCADE ON UPDATE CASCADE
);

