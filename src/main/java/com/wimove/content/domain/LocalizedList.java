/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 19 Nov 2009
 * mccalv
 * LocalizedItem
 * 
 */
package com.wimove.content.domain;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.localizedlist")
public class LocalizedList extends AbstractDomainObject implements Serializable {

	public LocalizedList(ItemList itemList, Label label, Locale locale, String value) {
		
		this.itemList = itemList;
		this.label = label;
		this.locale = locale;
		this.value = value;
	}

	public LocalizedList(){
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5124733507725310389L;

	public static enum Label {
		Title, Description

	}

	
	@ManyToOne
	@JoinColumn(name = "list_id")
	private ItemList itemList;

	@Enumerated(EnumType.STRING)
	@Column(name = "label")
	private Label label;

	@Column(name = "locale")
	@Type(type = "locale")
	private Locale locale;

	@Column(name = "value")
	private String value;

	

	/**
	 * @return the itemList
	 */
	public ItemList getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(ItemList itemList) {
		this.itemList = itemList;
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
}
