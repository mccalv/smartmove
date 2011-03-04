/*
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.time_occurrence")
public class TimeOccurrence extends AbstractDomainObject {

	public TimeOccurrence(Item item, Date startDate, Date endDate) {

		this.item = item;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public TimeOccurrence() {

	}

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;
	
	
	@Column(name = "address")
	private String address;

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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}
