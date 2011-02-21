SET search_path TO wimovegis,public


CREATE SEQUENCE id_seq START 101;
CREATE SEQUENCE users_id_users_seq START 1;
SELECT AddGeometryColumn( 'gps_position', 'geom_point', 4326,'POINT',2);
SELECT AddGeometryColumn( 'gidzone', 'polygon', 4326,'POLYGON',2);

insert into apikey (hash, domain) VALUES('qw33fvvtg5hh',null);

INSERT INTO roles (id_roles, code_role) VALUES(1,'Amministratore');
INSERT INTO roles (id_roles, code_role) VALUES(2,'Redattore');


INSERT INTO users (id_users, name, surname, email, nick, password, date_activation, last_login, enable) VALUES(2,'Redattore','Redattore','redattore@wimove.com','redattore','e10adc3949ba59abbe56e057f20f883e','2008-06-06 16:28:53','2008-06-06 16:28:53',	1);
INSERT INTO users (id_users, name, surname, email, nick, password, date_activation, last_login, enable) VALUES(1,'Amministratore','Amministratore','admin@wimove.com','admin','e10adc3949ba59abbe56e057f20f883e','2008-06-06 16:28:53',	'2008-06-06 16:28:53',1);

INSERT INTO roles_users (id_roles_users, id_roles, id_users) VALUES(1,1,1);
INSERT INTO roles_users (id_roles_users, id_roles, id_users) VALUES(1,2,2);



 	




SELECT  GeomFromText('POLYGON((0 0,0 10,10 10,10 0,0 0))', -1)

SELECT  GeomFromText('POLYGON((12.5637 41.8746,12.5651 41.9434,12.4765 41.9103,12.5637 41.8746))', 4326)


/*
 * Questa funziona
 */


GeomFromText('POINT(? ?)', 4326)

SELECT * FROM  gps_position WHERE 
ST_Contains ( GeomFromText('POLYGON((12.5637 41.8746,12.5651 41.9434,12.4765 41.9103,12.5637 41.8746))', 4326),geom_point)	



select * from gidzone where st_contains(polygon,GeomFromText('POINT(12.4718286395182 41.8480670551736)', 4326))


SELECT
 ST_Contains(
 GeomFromText('POLYGON((0 0,0 10,10 10,10 0,0 0))', -1),
 GeomFromText('POLYGON((5 5,5 6,6 6,6 5,5 5))', -1) ); 

GeomFromText('POLYGON((521526 5377783, 521481 5377811, 521494 5377832, 521539 5377804, 521526 5377783))', 32631))
SELECT GeomFromText('POLYGON((12.5637 41.8746, 12.5651 41.9434, 12.4765 41.9103))',4326)
	
ST_Contains(geometry geomA
SELECT * FROM  gps_position WHERE 
ST_Contains (GeomFromText('POLYGON(12.5637 41.8746, 12.5651 41.9434, 12.4765 41.9103)',4326),geom_point)