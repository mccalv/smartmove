/* Wimove project, 2009.
 * Java restful server for erogating the content. All rights reserved
 * 16 Nov 2009
 * mccalv
 * 
 */
package com.closertag.smartmove.server.content.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.extra")
public class Extra extends AbstractLabelValueModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -170483991422861892L;

	
}
