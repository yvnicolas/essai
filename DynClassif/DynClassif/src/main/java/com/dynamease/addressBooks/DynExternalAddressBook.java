package com.dynamease.addressBooks;

import java.util.Iterator;
import java.util.Set;

import com.dynamease.entity.DynSubscriber;

public interface DynExternalAddressBook  {
	
	public Set<String> retreiveCategories();
	
	/*
	 * Returns the next contact in the address book after rating the information from what can be found in the address book
	 * @see java.util.Iterator#next()
	 */


	public DynCatRatedPerson next(DynSubscriber sub);
	
	public boolean hasNext();
}
