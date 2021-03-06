#SELECT AddGeometryColumn('', 'gps_position','geom_point',-1,'POINT',2);


#SELECT AddGeometryColumn( 'gps_position', 'geom_point', -1, 'GEOMETRY', 2 );


SELECT AddGeometryColumn( 'gps_position', 'geom_point', 31370,'POINT',2);
SELECT AddGeometryColumn( 'gps_position', 'geom_point', 2163,'POINT',2);
SELECT AddGeometryColumn( 'gps_position','geom_point', 32661, 'POINT', 2 );

SELECT AddGeometryColumn( 'gps_position', 'geom_point', 4326,'POINT',2);





UPDATE gps_position SET geom_point = transform(PointFromText('POINT(' || longitude || ' ' || latitude || ')',4269),32661) ;

ALTER TABLE "public"."gps_position" ALTER COLUMN "point" TYPE geometry(1

SET search_path = public;

CREATE OR REPLACE FUNCTION autoinc() 
RETURNS trigger 
AS '$libdir/autoinc'
LANGUAGE C;
CREATE OR REPLACE SEQUENCE next_id START 1 MINVALUE 1;
CREATE OR REPLACE TRIGGER item_nextid 
	BEFORE INSERT  ON item
	FOR EACH ROW 
	EXECUTE PROCEDURE autoinc (id, next_id);
	
CREATE OR REPLACE TRIGGER gps_position_nextid 
	BEFORE INSERT  ON gps_position
	FOR EACH ROW 
	EXECUTE PROCEDURE autoinc (id, next_id);
	
select distance_spheroid(
    GeomFromText('POINT(41.8736  12.25006)', 4326),
      GeomFromText('POINT(42.95152 13.87812)', 4326),
      'SPHEROID["WGS_1984",6378137,298.257223563]')	
      
      ;
select distance_spheroid(
    GeomFromText('POINT(12.25006 41.8736 )'),
      GeomFromText('POINT(13.87812 42.95152 )'),
      'SPHEROID["WGS_1984",6378137,298.257223563]')	
      
      ;     

      select distance_spheroid(
    GeomFromText('POINT(41.8736 12.25006)'),
      GeomFromText('POINT(42.95152 13.87812)'),
      'SPHEROID["GRS_1980",6378137,298.257222101]')	
      
      ;     

      )
      
    select ST_Distance(
        ST_Transform(
                ST_SetSRID(ST_Point(-1.54911, 53.7996374), 4326),
                3003),
        ST_Transform(
                ST_SetSRID(ST_Point(-1.784876, 53.6451179), 4326),
                27700)
        ) / 1000;   
        
        
              
    select ST_Distance(
        ST_Transform(
                ST_SetSRID(ST_Point(41.8736, 12.25006), 4326),
                27700),
        ST_Transform(
                ST_SetSRID(ST_Point(42.95152, 13.87812), 4326),
                27700)
        ) / 1000;  
        
        select ST_Distance(
        (
               GeomFromText('POINT(41.8736  12.25006)', 4326),
      
       GeomFromText('POINT(42.95152 13.87812)', 4326)
        ) / 1000;  
         
        
        SELECT *
FROM geoname
WHERE st_dwithin(latlon_point, PointFromText('POINT(42.95152 13.87812)',4326),5000) 


        //* Distanzza da Roma e San Benedetto */
          select ST_Distance(
        ST_Transform(
                ST_SetSRID(ST_Point(12.25006,41.8736), 4326),
                27700),
        ST_Transform(
                ST_SetSRID(ST_Point(13.87812,42.95152 ), 4326),
                27700)
        ) / 1000;  

        
        
        CREATE OR REPLACE FUNCTION ST_DWithin_Sphere(geometry, geometry, float8)
        RETURNS boolean
        AS '
            SELECT $1 && ST_Expand($2,$3 * 1.79866403673916e-05) 
               AND $2 && ST_Expand($1,$3 * 1.79866403673916e-05) 
               AND ST_Distance($1, $2) < 
                        $3 / ST_Distance_Sphere(
                                   ST_MakePoint(0, ST_Y(ST_Centroid($1))), 
                                   ST_MakePoint(1, ST_Y(ST_Centroid($1))))
        ' LANGUAGE 'SQL' IMMUTABLE;

        
        
 /*       
  * Convert from WGS84 to Gauss.
  */
        select AsText(ST_Transform(
                ST_SetSRID(ST_Point(12.498950,41.897300), 4326),
                3004))
     POINT(2312519.5484826 4641480.79075838)           
         2312535.67107	4641405.85363
   POINT(2312519.5484826 4641480.79075838)
   
   Con SRID  26592
   POINT(2312519.5484826 4641480.79075838)
   
   
    select
       * FROM gidzone g
    where
        ST_Contains(g.polygon, PointFromText('POINT(12.4713136553873 41.9019988853709)',4326))   
   
   
   
   select
        this_.id as id12_1_,
        this_.creation_date as creation2_12_1_,
        this_.name as name12_1_,
        this_.update_date as update4_12_1_,
        gidzones3_.id_list as id1_,
        gidzones1_.id as id2_,
        gidzones1_.id as id14_0_,
        gidzones1_.identifier as identifier14_0_,
        gidzones1_.polygon as polygon14_0_ 
    from
        public.list this_ 
    inner join
        list_gidzone gidzones3_ 
            on this_.id=gidzones3_.id_list 
    inner join
        public.gidzone gidzones1_ 
            on gidzones3_.id_gidzone=gidzones1_.id 
    where
  
        st_touches(gidzones1_.polygon, PointFromText('POINT(12.4713136553873 41.9019988853709)',4326)) 
        
        
         select
        this_.id as id12_1_,
        this_.creation_date as creation2_12_1_,
        this_.name as name12_1_,
        this_.update_date as update4_12_1_,
        gidzones3_.id_list as id1_,
        gidzones1_.id as id2_,
        gidzones1_.id as id14_0_,
        gidzones1_.identifier as identifier14_0_,
        gidzones1_.polygon as polygon14_0_ 
    from
        public.list this_ 
    inner join
        list_gidzone gidzones3_ 
            on this_.id=gidzones3_.id_list 
    inner join
        public.gidzone gidzones1_ 
            on gidzones3_.id_gidzone=gidzones1_.id 
    where
        st_contains(gidzones1_.polygon, PointFromText('POINT(12.4713136553873 41.9019988853709)',4326)) 
        
        select
        this_.id as id12_1_,
        this_.creation_date as creation2_12_1_,
        this_.name as name12_1_,
        this_.update_date as update4_12_1_,
        gidzones3_.id_list as id1_,
        gidzones1_.id as id2_,
        gidzones1_.id as id14_0_,
        gidzones1_.identifier as identifier14_0_,
        gidzones1_.polygon as polygon14_0_ 
    from
        public.list this_ 
    inner join
        list_gidzone gidzones3_ 
            on this_.id=gidzones3_.id_list 
    inner join
        public.gidzone gidzones1_ 
            on gidzones3_.id_gidzone=gidzones1_.id 
    where
        st_contains(gidzones1_.polygon, PointFromText('POINT(12.4713134765625 41.902000427246094)',4326)) 
        
        