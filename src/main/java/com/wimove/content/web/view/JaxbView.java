/**
 * 
 */
package com.wimove.content.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.springframework.web.servlet.View;

import com.wimove.content.utils.JAXBUtils;
import com.wimove.constant.Constants;


/**
 * @author mccalv
 * 
 */
public class JaxbView implements View {

	String[] cdata = { "http://content.wimove.com/services^title",
			"http://content.wimove.com/services^name",
			"http://content.wimove.com/services^description",
			"http://content.wimove.com/services^address",
			"http://content.wimove.com/services^value" };

	/**
	 * The JAXB object to be sent as a response XML. This is our "model" in the
	 * MVC sense.
	 */
	private final Object jaxbObject;

	public JaxbView(Object reply) {
		this.jaxbObject = reply;
	}

	private String replyXML;

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
		String replyString = marshall();
		response.setContentType(getContentType());

		PrintWriter writer = response.getWriter();
		writer.write(replyString);
		writer.flush();
		response.flushBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.View#getContentType()
	 */
	public String getContentType() {
		return Constants.XML_CONTENT_TYPE;
	}

	private String marshall() throws JAXBException {
		replyXML = JAXBUtils.marshalToString(jaxbObject, cdata);
		return replyXML;

	}

}
