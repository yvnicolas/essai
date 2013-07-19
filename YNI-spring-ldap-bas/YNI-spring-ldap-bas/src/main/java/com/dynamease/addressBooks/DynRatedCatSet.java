package com.dynamease.addressBooks;

import java.util.HashMap;
import java.util.Map;

import com.dynamease.entity.*;

public class DynRatedCatSet {

	private Map <DynCategories, Integer> notes ;
	
	
	public DynRatedCatSet() {
		notes = new HashMap<DynCategories, Integer>();
	}
	
	public Integer getRate(DynCategories cat){
		return notes.get(cat);
		
	}
	
	public void setRate(DynCategories cat, Integer i) {
		notes.put(cat, i);
	}
	
}
