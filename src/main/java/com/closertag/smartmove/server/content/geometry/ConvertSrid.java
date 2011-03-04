/**
 * 
 */
package com.closertag.smartmove.server.content.geometry;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Utility class to convert different SRID
 * 
 * @author mccalv
 * 
 */
public class ConvertSrid {
	/**
	 * Convinience method to convert a GAUSS BOAGA project into a WGS84
	 * <p>
	 * <a href="http://it.wikipedia.org/wiki/Proiezione_di_Gauss-Boaga">Gauss
	 * Boaga (EST)</a> is identified by SRID 3004 while WGS84 is associated to
	 * SRID EPSG:4326.
	 * <p>
	 * Conversion between different SRID is affected by approssimation, so the
	 * exact value is unpredicable. The underly implementation is based on
	 * &quot;GeoTools&quot;
	 * 
	 * @throws IllegalArgumentException if the coordinates are passed in the wrong format
	 * @see Crs
	 * @param latlon
	 * 
	 * @return an array representing the coordinates,
	 */
	public static Double[] fromWgs84ToGaussBoaga(Double[] latlon) {

		CoordinateReferenceSystem crs;
		try {

			crs = CRS.decode("EPSG:4326");
			MathTransform mt = CRS.findMathTransform(crs, CRS.decode(
					"EPSG:3004", true));

			DirectPosition ll = new DirectPosition2D(latlon[0], latlon[1]);

			DirectPosition projected = mt.transform(ll, null);
			Double[] f = new Double[2];
			f[0] = projected.getOrdinate(0);
			f[1] = projected.getOrdinate(1);

			return f;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Impossible to convert the given coordinates "
					+ latlon + ". Are they expressed in the correct format? ");
		}
		
	}
		public static Double[] fromGaussBoagaToWgs84(Double[] latlon) {

			CoordinateReferenceSystem crs;
			try {

				crs = CRS.decode("EPSG:3004");
				MathTransform mt = CRS.findMathTransform(crs, CRS.decode(
						"EPSG:4326", true));

				DirectPosition ll = new DirectPosition2D(latlon[0], latlon[1]);

				DirectPosition projected = mt.transform(ll, null);
				Double[] f = new Double[2];
				f[0] = projected.getOrdinate(0);
				f[1] = projected.getOrdinate(1);

				return f;
			} catch (Exception ex) {
				throw new IllegalArgumentException("Impossible to convert the given coordinates "
						+ latlon + ". Are they expressed in the correct format? ");
			}

	}

}
