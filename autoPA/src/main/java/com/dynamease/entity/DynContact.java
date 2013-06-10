/**
 * 
 */
package com.dynamease.entity;

import java.util.List;

/**
 * @author yves
 * 
 */
public class DynContact extends DynPerson {
	
	// codage en dur de la base. A modifier par lookup sur l'annuaire.
	private final String baseDN = "dc=dynamease, dc=net"; 
	
	private String fullName;
	private List<String> categories;
	private DynSubscriber refDynSubscriber;
	
	public DynContact(String firstName, String lastName,
			DynSubscriber refDynSubscriber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
		this.refDynSubscriber = refDynSubscriber;
	}

	public DynContact(String fullName, DynSubscriber refDynSubscriber) {
		super();
		this.fullName = fullName;
		this.refDynSubscriber = refDynSubscriber;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public DynSubscriber getRefDynSubscriber() {
		return refDynSubscriber;
	}

	public void setRefDynSubscriber(DynSubscriber refDynSubscriber) {
		this.refDynSubscriber = refDynSubscriber;
	}

	public String constructDN() {
		return baseDN + ",cn=" + this.getRefDynSubscriber().getFullName() + ",cn= "+ this.getFullName();
	}
}