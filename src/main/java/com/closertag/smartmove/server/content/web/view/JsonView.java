/**
 * 
 */
package com.closertag.smartmove.server.content.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.springframework.web.servlet.View;

import com.closertag.smartmove.server.constant.Constants;
import com.closertag.smartmove.server.content.utils.JAXBUtils;

/**
 * @author mccalv
 * 
 */
public class JsonView implements View {

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

	public JsonView(Object reply) {
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
		response.setContentType(getContentType());

		PrintWriter writer = response.getWriter();

		ObjectMapper mapper = new ObjectMapper();

		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().setAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
		mapper.writeValue(writer, jaxbObject);

		writer.flush();
		response.flushBuffer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.View#getContentType()
	 */
	public String getContentType() {
		return Constants.JSON_CONTENT_TYPE;
	}

}
