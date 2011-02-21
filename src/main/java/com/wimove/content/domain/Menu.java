/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Dec 2009
 * mccalv
 * Menu
 * 
 */
package com.wimove.content.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.menu")
public class Menu extends AbstractDomainObject implements Serializable {

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
	

	
	@ManyToMany
	@JoinTable(name="list_menu" )
	private List<ItemList> itemList;
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
	 * @return the itemList
	 */
	public List<ItemList> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(List<ItemList> itemList) {
		this.itemList = itemList;
	}

	

	

}
