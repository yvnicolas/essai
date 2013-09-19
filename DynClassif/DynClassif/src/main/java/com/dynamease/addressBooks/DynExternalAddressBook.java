package com.dynamease.addressBooks;

import java.util.Set;

import com.dynamease.entity.DynSubscriber;

public interface DynExternalAddressBook  {
	
	public Set<String> retreiveCategories();
	
	public DynCatRatedPerson next(DynSubscriber sub);
	
	public boolean hasNext();
	
	public void setCategoryMapper(DynCategoryMapper mapper);
}
