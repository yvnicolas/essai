/**
 * 
 */
package com.dynamease.ldapBas;

/**
 * Concrete implementation of Person objects
 * 
 * @author yves
 * 
 */
public class DyniPerson {

	private String firstName = "";
	private String lastName = "";

	public DyniPerson(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	

	public DyniPerson() {
		super();
			}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

}
