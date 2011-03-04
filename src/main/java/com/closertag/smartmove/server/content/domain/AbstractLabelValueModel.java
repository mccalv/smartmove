/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 18 Nov 2009
 * mccalv
 * AbstractLabelValueModel
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.google.code.simplestuff.annotation.BusinessField;
import com.google.code.simplestuff.annotation.BusinessObject;

/**
 * @author mccalv
 * 
 */
@MappedSuperclass
@BusinessObject
public abstract class AbstractLabelValueModel extends AbstractDomainObject {

	@Column(name = "label")
	@BusinessField
	private String label;

	@Column(name = "value")
	@BusinessField
	private String value;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@BusinessField
	@Column(name = "locale")
	@Type(type = "locale")
	private Locale locale;

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
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
