/*
 * 
 */
package com.closertag.smartmove.server.content.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.closertag.smartmove.server.content.geometry.GeoLocHelper;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.gps_position")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class GpsPosition extends AbstractDomainObject {

	public GpsPosition() {

	}

	/**
	 * Default constuctor for a Gps Position. Its invokes the
	 * {@link GeoLocHelper} to create also the {@link #geom_point}
	 * 
	 * @param item
	 * @param latitude
	 * @param longitude
	 * @param address
	 */
	public GpsPosition(Item item, float latitude, float longitude,
			String address, String locality) {

		this.item = item;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.locality = locality;
		this.geom_point = GeoLocHelper.createPoint(longitude, latitude);
	}

	public GpsPosition(String address, String locality) {
		this.address = address;
		this.locality = locality;

	}

	public static final String LOCATION_COLUMN_NAME = "geom_point";

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "geom_point")
	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Point geom_point;

	@Column(name = "latitude")
	private float latitude;

	@Column(name = "longitude")
	private float longitude;

	@Column(name = "address")
	private String address;

	@Column(name = "locality")
	private String locality;

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return the geom_point
	 */
	public Point getGeom_point() {
		return geom_point;
	}

	/**
	 * @param geom_point
	 *            the geom_point to set
	 */
	public void setGeom_point(Point geom_point) {
		this.geom_point = geom_point;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality
	 *            the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

}
