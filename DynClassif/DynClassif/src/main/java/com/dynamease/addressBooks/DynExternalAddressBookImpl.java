package com.dynamease.addressBooks;

import java.util.Set;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

public abstract class DynExternalAddressBookImpl implements DynExternalAddressBook {

	@Override
	public abstract boolean hasNext();

	@Override
	public DynCatRatedPerson next(DynSubscriber sub) {
		
		DynCatRatedPerson toReturn = new DynCatRatedPerson(this.getNextEntry());
		for (DynCategories cat : DynCategories.values()) {
			toReturn.setRate(cat, this.rateCurrent(cat, sub));
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
	public abstract Set<String> retreiveCategories();
	
	/** to Be implemented by each address book to generate the DynPerson corresponding the the next connection from the information in the address book
	 * @return
	 */
	public abstract DynPerson getNextEntry();
	
	
	/**Used to get the rating note for a category concerning the current connection
	 * @param cat
	 * @param sub 
	 * @return
	 */
	public abstract int rateCurrent (DynCategories cat, DynSubscriber sub);

}
