/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Dec 2009
 * mccalv
 * Menu
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.list")
public class ItemList extends AbstractDomainObject implements Serializable,
		Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -853031825680271150L;

	@Column(name = "name")
	private String name;

	@Column(name = "creation_date")
	private Date creationDate = new Date();

	@Column(name = "update_date")
	private Date updateDate = new Date();

	@OneToMany(targetEntity = LocalizedList.class, cascade = CascadeType.ALL, mappedBy = "itemList")
	private List<LocalizedList> LocalizedLists = new ArrayList<LocalizedList>();

	@ManyToMany(targetEntity = GidZone.class, cascade = CascadeType.ALL)
	@JoinTable(name = "list_gidzone", joinColumns = { @JoinColumn(name = "id_list") }, inverseJoinColumns = { @JoinColumn(name = "id_gidzone") })
	private List<GidZone> gidZones = new ArrayList<GidZone>();

	@Transient
	private String listName;
	@Transient
	private String listDescription;

	@ManyToMany
	@JoinTable(name = "list_item")
	private List<Item> item;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the item
	 */
	public List<Item> getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(List<Item> item) {
		this.item = item;
	}

	/**
	 * @return the localizedLists
	 */
	public List<LocalizedList> getLocalizedLists() {
		return LocalizedLists;
	}

	/**
	 * @param localizedLists
	 *            the localizedLists to set
	 */
	public void setLocalizedLists(List<LocalizedList> localizedLists) {
		LocalizedLists = localizedLists;
	}

	/**
	 * @return the listName
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * @param listName
	 *            the listName to set
	 */
	public void setListName(String listName) {
		this.listName = listName;
	}

	/**
	 * @return the listDescription
	 */
	public String getListDescription() {
		return listDescription;
	}

	/**
	 * @param listDescription
	 *            the listDescription to set
	 */
	public void setListDescription(String listDescription) {
		this.listDescription = listDescription;
	}

	/**
	 * @return the gidZones
	 */
	public List<GidZone> getGidZones() {
		return gidZones;
	}

	/**
	 * @param gidZones
	 *            the gidZones to set
	 */
	public void setGidZones(List<GidZone> gidZones) {
		this.gidZones = gidZones;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo(Object n) {
	
		return listName.compareTo(((ItemList)n).getListName());

	}

	

}
