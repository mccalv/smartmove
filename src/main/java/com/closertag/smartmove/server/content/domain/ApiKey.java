/**
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.apikey")
public class ApiKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5789054472439565493L;

	@Id
	@Column(name = "hash")
	public String hash;

	@Column(name = "domain")
	private String domain;

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

}
