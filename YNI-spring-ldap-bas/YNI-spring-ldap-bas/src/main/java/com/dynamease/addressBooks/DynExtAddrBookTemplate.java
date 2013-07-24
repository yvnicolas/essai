package com.dynamease.addressBooks;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;

public class DynExtAddrBookTemplate extends DynExternalAddressBookImpl<String> {

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DynPerson getNextEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int rateCurrent(DynCategories cat) {
		// TODO Auto-generated method stub
		return 0;
	}

}
