package com.dynamease.addressBooks;

import java.util.Iterator;
import java.util.Set;

import com.dynamease.entity.DynPerson;

public interface DynExternalAddressBook extends Iterator<DynCatRatedPerson> {
	
	public Set<String> retreiveCategories();
	

}
