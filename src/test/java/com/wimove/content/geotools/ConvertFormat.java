package com.wimove.content.geotools;

import static org.junit.Assert.assertNotNull;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class ConvertFormat {
	/**
	 * A basic test for converting a EPSG:3004 To WGS84 transformation
	 * 
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 * @throws MismatchedDimensionException
	 * @throws TransformException
	 */
	@Test
	public void testAEPSGToWgs84() throws NoSuchAuthorityCodeException,
			FactoryException, MismatchedDimensionException, TransformException {

		CoordinateReferenceSystem crs;

		crs = CRS.decode("EPSG:3004", true);

		// find a maths transform from WGS84 to the target CRS
		MathTransform mt = CRS.findMathTransform(crs,
				DefaultGeographicCRS.WGS84);

		DirectPosition ll = new DirectPosition2D(2312535.67107, 4641405.85363);

		DirectPosition projected = mt.transform(ll, null);

		assertNotNull(projected);
		System.out.println("projected location: " + projected.getCoordinate());
		

	}

	@Test
	public void testDifferentCoordinatesSystem() throws Exception {

		CoordinateReferenceSystem crs;

		crs = CRS.decode("EPSG:3004", true);
		// String wkt2 = " crs = CRS.decode("EPSG:4326");
		String wkt2 = " PROJCS[\"unnamed\",GEOGCS[\"International 1909 (Hayford)\",DATUM[\"unknown\",SPHEROID[\"intl\",6378388,297]],PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.0174532925199433]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",9],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",2520000],PARAMETER[\"false_northing\",0]]";

		String wkt3 = "PROJCS[\"Transverse_Mercator\",GEOGCS[\"International 1909 (Hayford)\",DATUM[\"D_unknown\",SPHEROID[\"intl\",6378388,297]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",9],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",2520000],PARAMETER[\"false_northing\",0],UNIT[\"Meter\",1]]";
		// String wkt =
		// "PROJCS[\"Transverse_Mercator\",GEOGCS[\"International 1909 (Hayford)\",DATUM[\"D_unknown\",SPHEROID[\"intl\",6378388,297]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",9],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",1500000],PARAMETER[\"false_northing\",0],UNIT[\"Meter\",1]]";

		String wkt4 = "PROJCS[\"unnamed\","
				+ " GEOGCS[\"International 1909 (Hayford)\","
				+ "  DATUM[\"unknown\","
				+ "     SPHEROID[\"intl\",6378388,297]],"
				+ " PRIMEM[\"Greenwich\",0],"
				+ "UNIT[\"degree\",0.0174532925199433]],"
				+ "PROJECTION[\"Transverse_Mercator\"],"
				+ "PARAMETER[\"latitude_of_origin\",0],"
				+ "PARAMETER[\"central_meridian\",9],"
				+ "PARAMETER[\"scale_factor\",0.9996],"
				+ "PARAMETER[\"false_easting\",2520000],"
				+ "PARAMETER[\"false_northing\",0]]";
		;

		// SELECT AsText(transform(PointFromText('POINT(12.498950 41.897300)',
		// 4326), 94686))
		// select * FROM spatial_ref_sys where auth_name='sr-org'
		// 94686

		// select distinct(auth_name) FROM spatial_ref_sys

		String wkt = "GEOGCS["
				+ "\"WGS 84\","
				+ "  DATUM["
				+ "    \"WGS_1984\","
				+ "    SPHEROID[\"intl\",6378388,297,AUTHORITY[\"SR-ORG\",\"4686\"]],"
				+ "    TOWGS84[0,0,0,0,0,0,0],"
				+ "    AUTHORITY[\"EPSG\",\"6326\"]],"
				+ "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
				+ "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
				+ "  AXIS[\"Lat\",NORTH]," + "  AXIS[\"Long\",EAST],"
				+ "  AUTHORITY[\"EPSG\",\"4326\"]]";
		CoordinateReferenceSystem crs2 = CRS.parseWKT(wkt3);
		// EPSG:3004
		// find a maths transform from WGS84 to the target CRS
		MathTransform mt = CRS.findMathTransform(CRS.decode("EPSG:4326"), crs,
				true);

		// convert location 10.0 deg N, 4.0 deg E

		// 4641405.85363,2312535.67107
		// projected location: GeneralDirectPosition[2312522.528021119,
		// 4641412.171581715

		DirectPosition ll = new DirectPosition2D(41.897300, 12.498950);
		/*
		 * 12.498950 12.492630 12.482650 12.484920 41.897300 41.891000 41.895530
		 * 41.894650
		 */

		// 12.473330 41.898940

		//

		DirectPosition projected = mt.transform(ll, null);

		System.out.println("projected location: " + projected.getOrdinate(0)
				+ " " + projected.getOrdinate(1));
		// 12.498950013467306, 41.89730008068392

	}
}
