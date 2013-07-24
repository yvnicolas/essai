package com.dynamease.addressBooks;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynContact;
import com.dynamease.entity.DynSubscriber;
import com.dynamease.services.ldap.DynContactDaoInterface;
import com.dynamease.services.ldap.DynDirectoryAccessException;

@Service
public class DynCategorizer {

	private static final int minLevel = 60;

	private DynSubscriber sub;
	private DynExternalAddressBook addrBook;
	private DynCategories defaultcat;

	@Autowired
	private DynContactDaoInterface dynContactDao;
	
	public DynCategorizer() {
		
	}

//	public DynCategorizer(DynSubscriber sub, DynExternalAddressBook addrBook, DynCategories defaultcat) {
//		this.sub = sub;
//		this.addrBook = addrBook;
//		this.defaultcat = defaultcat;
//	}

	/**
	 * Main entry to the DynCategorizer. To be called on an external Address
	 * book For each contact in the address book, will compute the cateogries
	 * information
	 * 
	 * @param subscriber
	 * @param book
	 * @param mode
	 * @param defaultcat
	 */
	public void categorize(DynSubscriber subscriber, DynExternalAddressBook book, DynCatMode mode, DynCategories defaultcat) {

		// set up object
		this.sub = subscriber;
		this.addrBook = book;
		this.defaultcat = defaultcat;

		// Main loop on each contact in the address book
		while (!addrBook.hasNext()) {
			DynCatRatedPerson catContact = addrBook.next();
			DynContact toBeAdded;

			// Get potential match in Directory or create the new contact.
			toBeAdded = dynContactDao.findContact(catContact.getPerson(), subscriber);
			if (toBeAdded == null)
				toBeAdded = new DynContact(catContact.getPerson().getFirstName(), catContact.getPerson().getLastName(), subscriber);

			// updates categories based on mode, default cat and what has been
			// found in directory
			updateCat(toBeAdded, catContact, mode);

			// Updates the directory
			try {
				dynContactDao.addContact(toBeAdded);
			} catch (DynDirectoryAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	void updateCat(DynContact toBeAdded, DynCatRatedPerson catContact, DynCatMode mode) {

		DynCategories catFromBook = inferCat(catContact, mode);

		// If the categorization is not unknown, updates the categories in
		// contact properly from mode
		if (catFromBook != DynCategories.UNKNOWN) {
			switch (mode) {
			// Adds the found category to contact
			case FORCED:
			case AUTO:
			case LIGHTWEIGHT:
				toBeAdded.getCategories().add(catFromBook);

			case RESET:
			case STRONGAUTO:
				toBeAdded.setCategories(new HashSet<DynCategories>());
				toBeAdded.getCategories().add(catFromBook);

			}
		}

	}

	private DynCategories inferCat(DynCatRatedPerson contact, DynCatMode mode) {

		DynCategories toReturn = DynCategories.UNKNOWN;

		switch (mode) {

		case FORCED: // No use of what is infered from address book
		case RESET:
			return defaultcat;
		case AUTO: // default automatic mode : get the best categories from the
					// rated set
			return contact.getBest();
		case LIGHTWEIGHT: // gets the best one only if superior at a minimum
							// level
		case STRONGAUTO:
			toReturn = contact.getBest();
			if (contact.getRate(toReturn) < minLevel)
				return DynCategories.UNKNOWN;
			break;

		default:
			break;

		}

		return toReturn;

	}
}
