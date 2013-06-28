package com.dynamease.dyndemoandroapp.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * A subscriber to the Dynamease services.
 * 
 * @author antoine
 * 
 */
public class DynSubscriber extends DynPerson implements Serializable,
		Comparable<DynSubscriber> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;
	private int subscriberId;

	public DynSubscriber() {
		super("", "");
		this.setFullName("");
	}

	public DynSubscriber(String firstName, String lastName) {
		super(firstName, lastName);
		this.setFullName(firstName + " " + lastName);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DynSubscriber)) {
			return (false);
		} else {
			return this.subscriberId == ((DynSubscriber) obj).subscriberId;
		}
	}

	@Override
	public String toString() {
		String resultat = "DynSubscriber : ";
		resultat = resultat.concat(this.getLastName());
		resultat = resultat.concat(" ");
		resultat = resultat.concat(this.getFirstName());
		return (resultat);
	}

	/**
	 * @Brief Compare two DynSubscriber and sort them by alphabetically (First
	 *        with lastname, then firstname, then fullname, and if still the
	 *        same, by id).
	 * 
	 */
	@Override
	public int compareTo(DynSubscriber compared) {
		if (this.getLastName().compareTo(compared.getLastName()) != 0) {
			return this.getLastName().compareTo(compared.getLastName());
		} else if (this.getFirstName().compareTo(compared.getFirstName()) != 0) {
			return this.getFirstName().compareTo(compared.getFirstName());
		} else if (this.getFullName().compareTo(compared.getFullName()) != 0) {
			return this.getFullName().compareTo(compared.getFullName());
		} else {
			return this.getSubscriberId() - compared.getSubscriberId();
		}
	}
}
