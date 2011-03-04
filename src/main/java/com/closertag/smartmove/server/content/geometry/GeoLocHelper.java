/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 19 Nov 2009
 * mccalv
 * GeoLocHelper
 * 
 */
package com.closertag.smartmove.server.content.geometry;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.operation.TransformException;

import com.closertag.smartmove.server.constant.SRID;
import com.closertag.smartmove.server.content.exception.WimoveException;
import com.closertag.smartmove.server.content.exception.WimoveException.ExceptionType;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * Provides useful methods for geolocalisation
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeoLocHelper {

	private static final double COS0 = Math.cos(0);
	private static final double SIN90 = Math.sin(90);

	/**
	 * Create a JTS point from the specified longitude and latitude for the SRID
	 * (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
	 * @see SRID
	 * @param longitude
	 *            The longitude for the point
	 * @param latitude
	 *            The latitude for the point
	 * @return A jts point from the specified longitude and latitude
	 * @throws IllegalArgumentException
	 *             if latitude is not between -90 and 90, or longitude is not
	 *             between -180 and 180
	 */
	public static Point createPoint(Float longitude, Float latitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException(
					"Longitude should be between -180 and 180");
		}
		if (latitude < -90 || latitude > 90) {
			throw new IllegalArgumentException(
					"latitude should be between -90 and 90");
		}
		GeometryFactory factory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.maximumPreciseValue), com.closertag.smartmove.server.constant.SRID.WGS84_SRID
				.getSRID());
		Point point = (Point) factory.createPoint(new Coordinate(longitude,
				latitude));
		return point;
	}

	/**
	 * Creates a {@link Polygon} from a input string passed in the this way
	 * &quot;lon lat, lon lat, lon lat&quot;. The first and the last couple of
	 * the series has to be identical, otherwise the polygon is not closed.
	 * <p>
	 * A correct example is the followings: 12.5637 41.8746,12.5651
	 * 41.9434,12.4765 41.9103,12.5637 41.8746
	 * 
	 * @param latLonCommaSeparated
	 * @return
	 */
	public static Polygon createPolygonFromString(String latLonCommaSeparated) {
		try {
			GeometryFactory geometryFactory = new GeometryFactory(
					new PrecisionModel(PrecisionModel.FLOATING),
					com.closertag.smartmove.server.constant.SRID.WGS84_SRID.getSRID());
			WKTReader reader = new WKTReader(geometryFactory);
			Polygon polygon = (Polygon) reader.read("POLYGON(("
					+ latLonCommaSeparated + ")),4326");
			return polygon;
		} catch (Exception e) {
			throw new WimoveException(
					ExceptionType.AREA_INPUT_INVALID,
					"The position in case of area has to be provided in the following format: 12.5637 41.8746,12.5651 41.9434,12.4765 41.9103,12.5637 41.8746 (lon lat, lon lat,...)");
		}
	}

	/**
	 * Creates a point from a string representing lat and lon comma separated
	 * 
	 * @param latLon
	 * @return
	 */
	public static Point createPoint(String latLon) {
		String[] split = StringUtils.split(latLon, ",");
		try {
			return createPoint(Float.parseFloat(split[0]), Float
					.parseFloat(split[1]));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Create a JTS MultiLineString from the specified array of linestring for
	 * the SRID (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
	 * 
	 * example : {"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50
	 * 50)"}
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
	 * @see SRID
	 * @param wktLineStrings
	 *            The longitude for the point
	 * @return A MultilineStringObject from the specified array of linestring
	 * @throws IllegalArgumentException
	 *             if the string are not correct
	 */
	public static MultiLineString createMultiLineString(String[] wktLineStrings) {
		LineString[] lineStrings = new LineString[wktLineStrings.length];
		for (int i = 0; i < wktLineStrings.length; i++) {
			LineString ls;
			try {
				ls = (LineString) new WKTReader().read(wktLineStrings[i]);
			} catch (com.vividsolutions.jts.io.ParseException pe) {
				throw new IllegalArgumentException(wktLineStrings[i]
						+ " is not valid " + pe);
			} catch (ClassCastException cce) {
				throw new IllegalArgumentException(wktLineStrings[i]
						+ " is not a LINESTRING");
			}
			lineStrings[i] = ls;
		}

		GeometryFactory factory = new GeometryFactory(new PrecisionModel(
				PrecisionModel.FLOATING), com.closertag.smartmove.server.constant.SRID.WGS84_SRID
				.getSRID());
		MultiLineString multiLineString = (MultiLineString) factory
				.createMultiLineString(lineStrings);
		return multiLineString;
	}

	/**
	 * parse a string and return the corresponding double value, it accepts
	 * comma or point as decimal separator
	 * 
	 * @param number
	 *            the number with a point or a comma as decimal separator
	 * @return the float value corresponding to the parsed string
	 * @throws ParseException
	 *             in case of errors
	 */
	public static float parseInternationalDouble(String number)
			throws ParseException {
		NumberFormat nffrench = NumberFormat.getInstance(Locale.FRENCH);
		NumberFormat nfus = NumberFormat.getInstance(Locale.US);

		Number numberToReturn = number.indexOf(',') != -1 ? nffrench
				.parse(number) : nfus.parse(number);
		return numberToReturn.floatValue();
	}

	/**
	 * @param lat
	 *            the central latitude for the Polygon
	 * @param lng
	 *            the central longitude for the polygon
	 * @param distance
	 *            the distance in meters from the point to create the polygon
	 * @return a polygon / square with a side of distance , with the point
	 *         (long,lat) as centroid throws {@link RuntimeException} if an
	 *         error occured thros {@link IllegalArgumentException} if lat, long
	 *         or distance is not correct
	 */
	public static Polygon createPolygonBox(double lng, double lat,
			double distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException("distance is incorect : "
					+ distance);
		}

		double latrad = ((lat * Math.PI) / 180);
		double lngrad = ((lng * Math.PI) / 180);
		double angulardistance = distance
				/ com.closertag.smartmove.server.constant.Constants.RADIUS_OF_EARTH_IN_METERS;
		double latRadSinus = Math.sin(latrad);
		double latRadCosinus = Math.cos(latrad);
		double angularDistanceCosinus = Math.cos(angulardistance);
		double deltaYLatInDegrees = Math.abs(Math.asin(latRadSinus
				* angularDistanceCosinus + latRadCosinus
				* Math.sin(angulardistance) * COS0)
				- latrad);

		double deltaXlngInDegrees = Math.abs(Math.atan2(SIN90
				* Math.sin(angulardistance) * latRadCosinus,
				angularDistanceCosinus - latRadSinus * latRadSinus));

		lngrad = (lngrad + Math.PI) % (2 * Math.PI) - Math.PI;

		double latdeg = ((deltaYLatInDegrees * 180) / Math.PI);
		double lngdeg = ((deltaXlngInDegrees * 180) / Math.PI);

		double minX = lng - lngdeg;
		double maxX = lng + lngdeg;
		double minY = lat - latdeg;
		double maxY = lat + latdeg;

		WKTReader reader = new WKTReader();
		StringBuffer sb = new StringBuffer("POLYGON((");
		String polygonString = sb.append(minX).append(" ").append(minY).append(
				",").append(maxX).append(" ").append(minY).append(",").append(
				maxX).append(" ").append(maxY).append(",").append(minX).append(
				" ").append(maxY).append(",").append(minX).append(" ").append(
				minY).append("))").toString();

		try {
			Polygon polygon = (Polygon) reader.read(polygonString);
			polygon.setSRID(com.closertag.smartmove.server.constant.SRID.WGS84_SRID.getSRID());
			return polygon;
		} catch (com.vividsolutions.jts.io.ParseException e) {
			throw new RuntimeException("can not create Polygon for lat=" + lat
					+ " long=" + lng + " and distance=" + distance + " : " + e);
		}
	}

	/**
	 * @param hewewkbt
	 *            the string in hexa well know text
	 * @return the geometry type, or throw an exception if the string can not be
	 *         convert.the SRID will be {@link SRID#WGS84_SRID}
	 */
	public static Geometry convertFromHEXEWKBToGeometry(String hewewkbt) {
		try {
			WKBReader wkReader = new WKBReader();
			Geometry geometry = wkReader.read(hexToBytes(hewewkbt.trim()));
			geometry.setSRID(com.closertag.smartmove.server.constant.SRID.WGS84_SRID.getSRID());
			return geometry;
		} catch (com.vividsolutions.jts.io.ParseException e) {

			throw new RuntimeException(
					"error when convert HEXEWKB to Geometry", e);
		}

	}

	private static byte[] hexToBytes(String wkb) {
		// convert the String of hex values to a byte[]
		byte[] wkbBytes = new byte[wkb.length() / 2];

		for (int i = 0; i < wkbBytes.length; i++) {
			byte b1 = getFromChar(wkb.charAt(i * 2));
			byte b2 = getFromChar(wkb.charAt((i * 2) + 1));
			wkbBytes[i] = (byte) ((b1 << 4) | b2);
		}

		return wkbBytes;
	}

	/**
	 * Turns a char that encodes four bits in hexadecimal notation into a byte
	 * 
	 * @param c
	 *            the char to convert
	 * 
	 */
	public static byte getFromChar(char c) {
		if (c <= '9') {
			return (byte) (c - '0');
		} else if (c <= 'F') {
			return (byte) (c - 'A' + 10);
		} else {
			return (byte) (c - 'a' + 10);
		}
	}

	/**
	 * Returns the orthodromic distance Distance between 2 points
	 * 
	 * @param point
	 * @param circleCenter
	 * @return
	 */
	public static Double calculateMtDistance(Point point, Point circleCenter) {
		try {
			return JTS.orthodromicDistance(point.getCoordinate(), circleCenter
					.getCoordinate(), DefaultGeographicCRS.WGS84);
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
}
