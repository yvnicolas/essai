package com.dynamease.addressBooks;

import java.util.Set;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;

public abstract class DynExternalAddressBookImpl <T> implements DynExternalAddressBook {

	@Override
	public abstract boolean hasNext();

	@Override
	public DynCatRatedPerson next() {
		
		DynCatRatedPerson toReturn = new DynCatRatedPerson(this.getNextEntry());
		for (DynCategories cat : DynCategories.values()) {
			toReturn.setRate(cat, this.rateCurrent(cat));
		}
		return toReturn;
	}
//
//	@Override
//	public void remove() {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public Set<String> retreiveCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** to Be implemented by each address book to generate the DynPerson corresponding the the next connection from the information in the address book
	 * @return
	 */
	public abstract DynPerson getNextEntry();
	
	
	/**Used to get the rating note for a category concerning the current connection
	 * @param cat
	 * @return
	 */
	public abstract int rateCurrent (DynCategories cat);

}
