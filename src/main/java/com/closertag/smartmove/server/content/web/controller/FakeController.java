/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 15 Dec 2009
 * mccalv
 * FakeController
 * 
 */
package com.closertag.smartmove.server.content.web.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.closertag.smartmove.server.content.web.view.ServiceView;

/**
 * @author mccalv
 * 
 */
@Controller
@Component
@RequestMapping(value = "/fake/**")
public class FakeController {
	@RequestMapping("tempiAttesaBus")
	public View getXmlRpcTempiAttesa() throws Exception {
		return new ServiceView(FileUtils
				.readFileToString(new ClassPathResource(
						"tempiAttesaBusXmlRpc.xml").getFile()));
	}
}
