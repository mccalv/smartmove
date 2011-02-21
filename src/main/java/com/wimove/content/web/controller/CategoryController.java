/**
 * 
 */
package com.wimove.content.web.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimove.content.domain.Category;
import com.wimove.content.protocol.XmlCategories;
import com.wimove.content.protocol.XmlCategory;
import com.wimove.content.service.ApiKeyService;
import com.wimove.content.service.CategoryService;
import com.wimove.content.web.view.JaxbView;

/**
 * @author mccalv
 * 
 */
@Controller
@Component
@RequestMapping(value = "/services/get/GetCategories/**")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ApiKeyService apiKeyService;

	@RequestMapping("xml")
	public JaxbView getCategories(@RequestParam("api_key") String apiKey,
			@RequestParam("language") String language) {
		apiKeyService.checkValidApiKey(apiKey);
		List<Category> categories = categoryService.getCategories();

		return new JaxbView(getXmlCategories(categories, new Locale(language)));
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
