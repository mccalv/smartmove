/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 27 Nov 2009
 * mccalv
 * Gid
 * 
 */
package com.wimove.content.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * It's the Geo Referenced Provider. The provider is uniquely identified by its identifier. 
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.gid")
public class Gid {
	@Id
	@Column(name = "identifier")
	private String identifier;

	@Column(name = "name")
	private String name;

	@OneToMany(targetEntity = Item.class, cascade = CascadeType.ALL, mappedBy = "gid")
	private List<Item> items = new ArrayList<Item>();

	public Gid(String identifier) {
		this.identifier = identifier;
	}

	public Gid() {

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
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return identifier.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return identifier.equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}
