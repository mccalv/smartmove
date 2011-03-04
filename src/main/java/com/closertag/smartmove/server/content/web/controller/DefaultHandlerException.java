/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 27 Nov 2009
 * mccalv
 * DefaultHandlerException
 * 
 */
package com.closertag.smartmove.server.content.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.closertag.smartmove.server.content.exception.WimoveException;
import com.closertag.smartmove.server.content.exception.WimoveException.ExceptionType;
import com.closertag.smartmove.server.content.web.view.JaxbView;
import com.wimove.content.protocol.XmlError;

/**
 * @author mccalv
 * 
 */
@Component
public class DefaultHandlerException implements HandlerExceptionResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerExceptionResolver#resolveException
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		XmlError xmlError = new XmlError();
		
		
		ex.printStackTrace();
		
		
		if (ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
			xmlError.setCode(ExceptionType.MISSING_PARAMETER.getCode());
		} else if (ex instanceof WimoveException) {

			xmlError.setCode(((WimoveException) ex).getExceptionType()
					.getCode());
			
			

		} else {

			xmlError.setCode(ExceptionType.GENERIC_EXCEPTION.getCode());

		}
		xmlError.setMessage(ex.getMessage());
		return new ModelAndView(new JaxbView(xmlError));
	}

}
