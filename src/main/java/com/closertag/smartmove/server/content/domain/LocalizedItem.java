/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 19 Nov 2009
 * mccalv
 * LocalizedItem
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.localizeditem")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class LocalizedItem extends AbstractDomainObject implements Serializable {

	public LocalizedItem(Item item, Label label, Locale locale, String value) {
		
		this.item = item;
		this.label = label;
		this.locale = locale;
		this.value = value;
	}

	public LocalizedItem(){
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5124733507725310389L;

	public static enum Label {
		Title, Description,Mp3

	}

	
	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Enumerated(EnumType.STRING)
	@Column(name = "label")
	private Label label;

	@Column(name = "locale")
	@Type(type = "locale")
	private Locale locale;

	@Column(name = "value")
	private String value;

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
