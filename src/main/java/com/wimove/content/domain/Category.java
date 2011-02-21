/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 17 Dec 2009
 * mccalv
 * Category
 * 
 */
package com.wimove.content.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.core.io.ClassPathResource;

import com.wimove.content.domain.LocalizedItem.Label;

/**
 * @author mccalv
 * 
 */
@SuppressWarnings("unchecked")
@Entity
@Table(name = "public.category")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Category {
	private static HashMap<String, Category> categories = new HashMap<String, Category>();

	static {

		try {

			File file = new ClassPathResource("ALL_CATEGORIES.csv").getFile();
			List<String> lines = FileUtils.readLines(file);
			for (String line : lines) {
				String[] cat = StringUtils.split(
						StringUtils.replace(line, "\"", ""), "|");
				Category baseCategory = new Category(cat[0]);
				LocalizedItem localizedItem = new LocalizedItem();
				localizedItem.setLabel(Label.Title);
				localizedItem.setLocale(Locale.ITALIAN);

				LocalizedItem localizedItem_uk = new LocalizedItem();
				localizedItem_uk.setLabel(Label.Title);
				localizedItem_uk.setLocale(Locale.ENGLISH);
				if (cat.length == 3) {
					localizedItem.setValue(StringUtils.capitalize(cat[1]));
					localizedItem_uk.setValue(StringUtils.capitalize(cat[2]));
				} else {
					localizedItem.setValue(StringUtils.capitalize(cat[0]));
					localizedItem_uk.setValue(StringUtils.capitalize(cat[1]));

				}

				baseCategory.getLabes().add(localizedItem);
				baseCategory.getLabes().add(localizedItem_uk);
				/*
				 * In this case the element [0] is the Italian category and [1]
				 * the English one
				 */

				categories.put(cat[0], baseCategory);

			}
		} catch (IOException e) {
			// LOG.error("Impossible to parse the ZETEMA_CATEGORIES file ");

			e.printStackTrace();
		}

	}

	public Category() {

	}

	public Category(String category) {
		this.category = category;
	}

	@Id
	@Column(name = "category")
	private String category;

	@Column(name = "description")
	private String description;
	@Transient
	private List<LocalizedItem> labes = new ArrayList<LocalizedItem>();

	/**
	 * Getter for labes.
	 * 
	 * @return the labes.
	 */
	public List<LocalizedItem> getLabes() {
		return labes;
	}

	/**
	 * @param labes
	 *            the labes to set
	 */
	public void setLabes(List<LocalizedItem> labes) {
		this.labes = labes;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	@Transient
	private Category base;
	@Transient
	private Locale locale;

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the base
	 */
	public Category getBase() {
		return base;
	}

	/**
	 * @param base
	 *            the base to set
	 */
	public void setBase(Category base) {
		this.base = base;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public static String getLabel(String identifier, Locale locale) {

		Category category2 = categories.get(identifier);
		if (category2 == null) {
			return null;

		}
		for (LocalizedItem l : category2.getLabes()) {
			if (l.getLocale().equals(locale)) {

				return l.getValue();
			}

		}
		return identifier;

	}
}
