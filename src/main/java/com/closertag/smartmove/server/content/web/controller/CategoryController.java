/**
 * 
 */
package com.closertag.smartmove.server.content.web.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.closertag.smartmove.server.content.domain.Category;
import com.closertag.smartmove.server.content.service.ApiKeyService;
import com.closertag.smartmove.server.content.service.CategoryService;
import com.closertag.smartmove.server.content.web.view.JaxbView;
import com.closertag.smartmove.server.content.web.view.JsonView;
import com.wimove.content.protocol.XmlCategories;
import com.wimove.content.protocol.XmlCategory;

/**
 * @author mccalv
 * 
 */
@Controller
@Component
@RequestMapping(value = "/services/get/getCategories/**")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ApiKeyService apiKeyService;

	@RequestMapping("xml")
	public View getCategories(HttpServletRequest req,@RequestParam("api_key") String apiKey,
			@RequestParam("language") String language) {
		apiKeyService.checkValidApiKey(req,apiKey);
		List<Category> categories = categoryService.getCategories();

		return new JaxbView(getXmlCategories(categories, new Locale(language)));
	}
	@RequestMapping("json")
	public View getCategoriesJson(HttpServletRequest req,@RequestParam("api_key") String apiKey,
			@RequestParam("language") String language) {
		apiKeyService.checkValidApiKey(req,apiKey);
		List<Category> categories = categoryService.getCategories();

		return new JsonView(getXmlCategories(categories, new Locale(language)));
	}

	private XmlCategories getXmlCategories(List<Category> categories,
			Locale locale) {
		XmlCategories xmlCategories = new XmlCategories();
		for (Category category : categories) {
			XmlCategory xmlCategory = new XmlCategory();
			xmlCategory.setIdentifier(category.getCategory());
			xmlCategory.setDescription(Category.getLabel(category.getCategory(),locale));
			
			//xmlCategories.getDescription().add(Category.getLabel(category.getCategory(),locale));
			xmlCategories.getCategory().add(xmlCategory);
		}
		return xmlCategories;
	}
}
