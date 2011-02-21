/**
 * 
 */
package com.wimove.content.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An Item is a generic container of informations related to a single Point of
 * Interest or an Event located in {@link GpsPosition} or and Area.
 * <p>
 * It's linked to the owner {@link Gid}. The repositories need to initialize the
 * dependencies.
 * 
 * @author mccalv
 * 
 */
@Entity
@Table(name = "public.item")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Item  implements Serializable {
	@SequenceGenerator(name = "item_seq", sequenceName = "id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
	@Column(name = "id")
	@Id
	protected  Long id;
	
	
	@Override
	public String toString() {
		// TODO Define the method selecting the correct fields
		return super.toString();
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 155167624791480306L;

	/*
	 * If you want to enable the autoincrement
	 */

	@Transient
	private String name;

	@Transient
	private String description;
	
	@Transient
	private String mp3;

	@Transient
	private Float distance;

	@Column(name = "website")
	private String website;

	@Column(name = "email")
	private String email;

	@Column(name = "item_id")
	private String itemId;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "creation_date")
	private Date creationDate = new Date();

	@Column(name = "update_date")
	private Date updateDate = new Date();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "gid_identifier")
	private Gid gid = new Gid();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	private Category category = new Category();

	@OneToMany(targetEntity = GpsPosition.class, cascade = CascadeType.ALL, mappedBy = "item")
	
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<GpsPosition> gpsPositions = new ArrayList<GpsPosition>();

	@OneToMany(targetEntity = Cost.class, cascade = CascadeType.ALL, mappedBy = "item")
	private List<Cost> costs = new ArrayList<Cost>();

	@OneToMany(targetEntity = Contact.class, cascade = { CascadeType.ALL}, mappedBy = "item")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Contact> contacts = new ArrayList<Contact>();

	@OneToMany(targetEntity = Extra.class, cascade = CascadeType.ALL, mappedBy = "item")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Extra> extras = new ArrayList<Extra>();

	@OneToMany(targetEntity = LocalizedItem.class, cascade = CascadeType.ALL, mappedBy = "item")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	/*
	 http://www.jroller.com/eyallupu/entry/hibernate_exception_simultaneously_fetch_multiple
	 */
//	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<LocalizedItem> localizedItems = new ArrayList<LocalizedItem>();

	@OneToMany(targetEntity = TimeOccurrence.class, cascade = CascadeType.ALL, mappedBy = "item")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("startDate DESC")
	private List<TimeOccurrence> timeOccurrences = new ArrayList<TimeOccurrence>();

	@ManyToMany(targetEntity = Tag.class, cascade = CascadeType.ALL)
	@JoinTable(name = "item_tag", joinColumns = { @JoinColumn(name = "item_id") }, inverseJoinColumns = { @JoinColumn(name = "tag") })
	private List<Tag> tags = new ArrayList<Tag>();
	
	
	

	@ManyToMany
	@JoinTable(name = "public.list_item", joinColumns = { @JoinColumn(name = "id_item") }, inverseJoinColumns = { @JoinColumn(name = "id_list") })
	private List<ItemList> itemList;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the distance
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the gpsPositions
	 */
	public List<GpsPosition> getGpsPositions() {
		return gpsPositions;
	}

	/**
	 * @param gpsPositions
	 *            the gpsPositions to set
	 */
	public void setGpsPositions(List<GpsPosition> gpsPositions) {
		this.gpsPositions = gpsPositions;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website
	 *            the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the costs
	 */
	public List<Cost> getCosts() {
		return costs;
	}

	/**
	 * @param costs
	 *            the costs to set
	 */
	public void setCosts(List<Cost> costs) {
		this.costs = costs;
	}

	/**
	 * @return the contacts
	 */
	public List<Contact> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts
	 *            the contacts to set
	 */
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the localizedItems
	 */
	public List<LocalizedItem> getLocalizedItems() {
		return localizedItems;
	}

	

	
	/**
	 * @param localizedItems
	 *            the localizedItems to set
	 */
	public void setLocalizedItems(List<LocalizedItem> localizedItems) {
		this.localizedItems = localizedItems;
	}

	/**
	 * @return the timeOccurrences
	 */
	public List<TimeOccurrence> getTimeOccurrences() {
		return timeOccurrences;
	}

	/**
	 * @param timeOccurrences
	 *            the timeOccurrences to set
	 */
	public void setTimeOccurrences(List<TimeOccurrence> timeOccurrences) {
		this.timeOccurrences = timeOccurrences;
	}

	/**
	 * @return the extras
	 */
	public List<Extra> getExtras() {
		return extras;
	}

	/**
	 * @param extras
	 *            the extras to set
	 */
	public void setExtras(List<Extra> extras) {
		this.extras = extras;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the gid
	 */
	public Gid getGid() {
		return gid;
	}

	/**
	 * @param gid
	 *            the gid to set
	 */
	public void setGid(Gid gid) {
		this.gid = gid;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the itemList
	 */
	public List<ItemList> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList
	 *            the itemList to set
	 */
	public void setItemList(List<ItemList> itemList) {
		this.itemList = itemList;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	

	/**
	 * Returns all the {@link LocalizedItem} for a given {@link Locale}
	 * 
	 * @param locale
	 * @return
	 */
	public List<LocalizedItem> getLocalizedItems(Locale locale) {
		List<LocalizedItem> retLocalizedItems = new ArrayList<LocalizedItem>();
		for (LocalizedItem extra : localizedItems) {
			if (extra.getLocale().equals(locale)) {
				retLocalizedItems.add(extra);
			}
		}
		return retLocalizedItems;
	}

	public List<AbstractLabelValueModel> getCosts(Locale locale) {
		List<AbstractLabelValueModel> returnCosts = new ArrayList<AbstractLabelValueModel>();
		for (Cost cost : costs) {
			if (cost.getLocale().equals(locale)) {
				returnCosts.add(cost);
			}
		}
		return returnCosts;
	}

	public List<AbstractLabelValueModel> getContacts(Locale locale) {
		List<AbstractLabelValueModel> returnContacts = new ArrayList<AbstractLabelValueModel>();
		for (Contact contact : contacts) {
			if (contact.getLocale().equals(locale)) {
				returnContacts.add(contact);
			}
		}
		return returnContacts;
	}
	/**
	 * Returns all the {@link Extra} for a given {@link Locale}
	 * 
	 * @param locale
	 * @return
	 */
	public List<AbstractLabelValueModel> getExtras(Locale locale) {
		List<AbstractLabelValueModel> returnExtras = new ArrayList<AbstractLabelValueModel>();
		for (Extra extra : extras) {
			if (extra.getLocale().equals(locale)) {
				returnExtras.add(extra);
			}
		}
		return returnExtras;
	}

	/**
	 * @return the mp3
	 */
	public String getMp3() {
		return mp3;
	}

	/**
	 * @param mp3 the mp3 to set
	 */
	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}
}
