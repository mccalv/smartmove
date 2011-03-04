package com.closertag.smartmove.server.content.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author mccalv
 * 
 */

@Entity
@Table(name = "public.tag")
/*
 * A group of named query to build the Tag cloud
 */
@NamedQueries( {
		@NamedQuery(name = "Tag.All", query = "from Tag t order by t.tag"),
		@NamedQuery(name = "Tag.CountAll", query = "select count(t.id) from Tag t"),
		@NamedQuery(name = "Tag.ByName", query = "from Tag t where t.tag = ?"),
		@NamedQuery(name = "Tag.AllTagStatistics", query = "select t, t.items.size from Tag t   order by t.tag"),
		@NamedQuery(name = "Tag.MostDocuments", query = "select max(t.items.size) from Tag t") })
public class Tag {

	@Id
	@Column(name = "tag")
	private String tag;

	@Column(name = "description")
	private String description;
	
	

	@ManyToMany(targetEntity = com.closertag.smartmove.server.content.domain.Item.class)
	@JoinTable(name = "item_tag", joinColumns = { @JoinColumn(name = "tag") }, inverseJoinColumns = { @JoinColumn(name = "item_id") })
	private List<Item> items = new ArrayList<Item>();

	/**
	 * A simple constructor to set the fields
	 * 
	 * @param tag
	 * @param description
	 */
	public Tag(String tag, String description) {
		this.tag = tag;
		this.description = description;
	}

	public Tag(String tag) {
		this.tag = tag;

	}

	public Tag() {
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return this.tag;
	}

}
