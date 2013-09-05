package com.closertag.smartmove.server.content.geometry;

/**
 * Helper method to
 * 
 * @author mccalv
 * 
 */
public class PointPositionHelper {

	private final static double EARTH_RADIUS_KM = 6378;
	private static final double DOUBLE_TO_GRADIENT_FACTOR = 57.29577951;

	/**
	 * A static holder for a point position
	 * 
	 * @author mccalv
	 * 
	 */

	public static class Point {
		private double lon;
		private double lat;

		public Point(double lat, double lon) {
			this.lat = lat;
			this.lon = lon;
		}

		/**
		 * @return the lat
		 */
		public double getLat() {
			return lat / DOUBLE_TO_GRADIENT_FACTOR;
		}

		/**
		 * @param lat
		 *            the lat to set
		 */
		public void setLat(double lat) {
			this.lat = lat;
		}

		/**
		 * @return the lon
		 */
		public double getLon() {
			return lon / 57.29577951;
		}

		/**
		 * @param lon
		 *            the lon to set
		 */
		public void setLon(double lon) {
			this.lon = lon;
		}

		public boolean isLatMin(Point secondPoint) {
			return this.lat < secondPoint.getLat();
		}

		public boolean isLongMin(Point secondPoint) {
			return this.lon < secondPoint.getLon();
		}

		public boolean isLatMan(Point secondPoint) {
			return this.lat > secondPoint.getLat();
		}

		public boolean isLongMan(Point secondPoint) {
			return this.lon > secondPoint.getLon();
		}
	}

	public static class Rectangle {
		private Point topleft;
		private Point topright;
		private Point bottomleft;
		private Point bottomright;

		public Rectangle(Point topleft, Point topright, Point bottomleft,
				Point bottomright) {

			this.topleft = topleft;
			this.topright = topright;
			this.bottomleft = bottomleft;
			this.bottomright = bottomright;
		}

		/**
		 * @return the topleft
		 */
		public Point getTopleft() {
			return topleft;
		}

		/**
		 * @param topleft
		 *            the topleft to set
		 */
		public void setTopleft(Point topleft) {
			this.topleft = topleft;
		}

		/**
		 * @return the topright
		 */
		public Point getTopright() {
			return topright;
		}

		/**
		 * @param topright
		 *            the topright to set
		 */
		public void setTopright(Point topright) {
			this.topright = topright;
		}

		/**
		 * @return the bottomleft
		 */
		public Point getBottomleft() {
			return bottomleft;
		}

		/**
		 * @param bottomleft
		 *            the bottomleft to set
		 */
		public void setBottomleft(Point bottomleft) {
			this.bottomleft = bottomleft;
		}

		/**
		 * @return the bottomright
		 */
		public Point getBottomright() {
			return bottomright;
		}

		/**
		 * @param bottomright
		 *            the bottomright to set
		 */
		public void setBottomright(Point bottomright) {
			this.bottomright = bottomright;
		}

	}

	/**
	 * Calculate the distance in Kilometers from 2 {@link Point}
	 * 
	 * <pre>
	 * &amp;acos(cos($a1)*cos($b1)*cos($a2)*cos($b2) +
	 * cos($a1)*sin($b1)*cos($a2)*sin($b2) + sin($a1)*sin($a2)) * $r;
	 * 	
	 * 
	 * &#064;param point
	 * &#064;param circleCenter
	 * &#064;param ray
	 * @return
	 */
	public static double calculateKmDistance(Point point, Point circleCenter) {

		double $a1 = point.getLat();
		double $a2 = circleCenter.getLat();
		double $b1 = point.getLon();
		double $b2 = point.getLon();

		double distance = Math
				.acos(Math.cos($a1) * Math.cos($b1) * Math.cos($a2)
						* Math.cos($b2) + Math.cos($a1) * Math.sin($b1)
						* Math.cos($a2) * Math.sin($b2) + Math.sin($a1)
						* Math.sin($a2))
				* EARTH_RADIUS_KM;
		return distance;

	}

	/**
	 * Verifies if a point is inside a given Rectangle
	 * 
	 * @param point
	 * @param rectangle
	 * @param ray
	 * @return
	 */
	public static boolean isInsideARettangle(Point point, Rectangle rectangle,
			float ray) {
		Point topleft = rectangle.getTopleft();
		Point topright = rectangle.getTopright();
		Point bottomleft = rectangle.getBottomleft();
		Point bottomright = rectangle.getBottomright();

		if ((point.isLongMan(topright) && point.isLongMin(bottomleft) && (point
				.isLatMan(topleft) && point.isLatMin(bottomright)))) {
			return true;
		}

		return false;
	}
}
