package com.dynamease.addressBooks;

import com.dynamease.entity.DynPerson;


	public class DynCSVContact extends DynPerson {

		public DynCSVContact(String firstName, String lastName,String categories) {
			super(firstName, lastName);
			this.categories = categories;
		}


		public DynCSVContact() {
			super();
				}
		
		
		public String getCategories() {
			return categories;
		}
		public void setCategories(String categories) {
			this.categories = categories;
		}
		private static final long serialVersionUID = 1L;
		private String categories;
		
		
	}

