package com.dynamease.addressBooks;

import com.dynamease.entity.DynPerson;

public class DynCatRatedPerson {
	private DynPerson person;
	private DynRatedCatSet rates;
	
	
	/**Prepares a void categorized person
	 * @param person
	 */
	public DynCatRatedPerson(DynPerson person) {
		this.person = person;
		this.rates = new DynRatedCatSet();
	}


	public DynPerson getPerson() {
		return person;
	}


	public void setPerson(DynPerson person) {
		this.person = person;
	}


	public DynRatedCatSet getRates() {
		return rates;
	}


	public void setRates(DynRatedCatSet rates) {
		this.rates = rates;
	}
	
	

}
