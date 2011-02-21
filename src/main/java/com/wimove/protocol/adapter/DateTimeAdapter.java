/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 23 Nov 2009
 * mccalv
 * DateTimeAdapter
 * 
 */
package com.wimove.protocol.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * @author mccalv
 * 
 */
public class DateTimeAdapter {

	public static Date parseDate(String s) {
		return DatatypeConverter.parseDate(s).getTime();
	}

	public static String printDate(Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		return df.format(dt);

	}

}