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

	public String buildFullName() {
		return this.firstName + " " + this.lastName;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DyniPerson other = (DyniPerson) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return String.format("DyniPerson [firstName=%s, lastName=%s]", firstName, lastName);
	}

}
