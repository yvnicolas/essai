package com.dynamease.addressBooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;

public class DynCatRatedPerson {
	private DynPerson person;
	// private DynRatedCatSet rates;
	private Map<DynCategories, Integer> notes;

	/**
	 * Prepares a void categorized person
	 * 
	 * @param person
	 */
	public DynCatRatedPerson(DynPerson person) {
		this.person = person;
		this.notes = new HashMap<DynCategories, Integer>();
	}

	public DynPerson getPerson() {
		return person;
	}

	public void setPerson(DynPerson person) {
		this.person = person;
	}

	public Integer getRate(DynCategories cat) {
		return notes.get(cat);

	}

	public void setRate(DynCategories cat, Integer i) {
		notes.put(cat, i);
	}

	/**Identifies the best categories in the map
	 * If several matches, returns the last match
	 * @return null if no categories can be identified
	 */
	public DynCategories getBest() {

		DynCategories toReturn = null;
		Integer max = 0;

		for (Entry<DynCategories, Integer> entry : notes.entrySet()) {
			if (entry.getValue() > max) {

				max = entry.getValue();
				toReturn = entry.getKey();
			}
		}
		return toReturn;

	}
}
