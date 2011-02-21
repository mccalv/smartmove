/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 7 Dec 2009
 * mccalv
 * ServiceView
 * 
 */
package com.wimove.content.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.wimove.constant.Constants;

/**
 * @author mccalv
 * 
 */
public class ServiceView implements View {

	private final String reply;

	public ServiceView(String reply) {
		this.reply = reply;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.View#getContentType()
	 */
	@Override
	public String getContentType() {
		return Constants.XML_CONTENT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.View#render(java.util.Map,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType(getContentType());

		PrintWriter writer = response.getWriter();
		writer.write(reply);
		writer.flush();
		response.flushBuffer();

	}

}
