package com.dynamease.addressBooks;

import java.util.Set;

import com.dynamease.entity.DynPerson;

public abstract class DynExternalAddressBookImpl <T> implements DynExternalAddressBook {

	@Override
	public abstract boolean hasNext();

	@Override
	public DynCatRatedPerson next() {
		
		DynCatRatedPerson toReturn = new DynCatRatedPerson(this.getNextEntry());
		return toReturn;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> retreiveCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public abstract DynPerson getNextEntry();

}
