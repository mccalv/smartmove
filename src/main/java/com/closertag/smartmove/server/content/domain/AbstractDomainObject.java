/**
 * 
 */
package com.closertag.smartmove.server.content.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;



/**
 * @author mccalv
 *
 */
@MappedSuperclass
public abstract class AbstractDomainObject {

	@SequenceGenerator(name = "item_seq", sequenceName = "id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
	@Column(name = "id")
	@Id
	protected  Long id;
	
	
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
}
