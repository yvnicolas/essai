package com.dynamease.dyndemoandroapp.entity;

import java.io.Serializable;

/**
 * Definition of a user that will be parsed into Json. (POJO)
 * 
 * @author Antoine
 */

public class DynPerson implements Serializable {
	private String firstName;
	private String lastName;

	public DynPerson() {
	}

	/**
	 * @param firstName
	 * @param lastName
	 */
	public DynPerson(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DynPerson [ firstName = " + firstName + ", lastName = "
				+ lastName + "]";
	}
}
