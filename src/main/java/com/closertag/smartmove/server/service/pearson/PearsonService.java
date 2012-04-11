/*
 * @(#)PearsonService.java     Apr 11, 2012
 *
 * Copyright (c) 2010 Innovation Engineering S.r.l. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.closertag.smartmove.server.service.pearson;

/**
 *
 * @author mccalv
 * @since Apr 11, 2012
 *
 */
public interface PearsonService {
	
	/**
	 * Import content form a specific couple lat and long
	 * @param lat
	 * @param lon
	 */
	void importContentsFromGps(Double lat,Double lon)throws Exception;
	

}
