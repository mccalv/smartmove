/**
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Polygon;

/**
 * The GidZone is essentially an area with a list of {@link ItemList} related
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.gidzone")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class GidZone implements Serializable {
	
	
	
	@SequenceGenerator(name = "item_seq", sequenceName = "id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
	@Column(name = "id")
	@Id
	protected Long id;

	@Override
	public String toString() {
		// TODO Define the method selecting the correct fields
		return super.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 155167624791480306L;

	public static final String POLYGON_COLUMN_NAME = "polygon";

	/*
	 * If you want to enable the autoincrement
	 */

	@Column(name = POLYGON_COLUMN_NAME)
	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Polygon polygon;

	@Column(name = "identifier")
	private String identifier;

	/*@ManyToMany
	@JoinTable(name = "public.list_gidzone", joinColumns = { @JoinColumn(name = "id_gidzone") }, inverseJoinColumns = { @JoinColumn(name = "id_list") })
	@OrderBy(clause ="ord desc")
	private List<ItemList> itemList;
 */
	/**
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		return polygon;
	}

	/**
	 * @param polygon
	 *            the polygon to set
	 */
	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	

}
