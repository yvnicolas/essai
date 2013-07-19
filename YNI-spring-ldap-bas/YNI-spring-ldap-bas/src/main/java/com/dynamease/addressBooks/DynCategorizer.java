package com.dynamease.addressBooks;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynSubscriber;

public class DynCategorizer {

	private DynSubscriber sub;
	private DynExternalAddressBook addrBook;
	private DynCategories defaultcat;

	private void doCategorize(DynCatMode mode) {

		while (!addrBook.hasNext()) {

			DynCategories result;

			DynCatRatedPerson catContact = addrBook.next();

			switch (mode) {

			case FORCED:
				result = defaultcat;
				break;

			case AUTO:
				// TODO si on est en auto, on prend la catégorie la plus forte
				break;

			case LIGHTWEIGHT:
				// TODO : on met eventuellement unknown
				break;

			default:
				break;

			}
			
			// On recupere le contact de l'annuaire
			
			// On met à jour les catégories
			
			// On réécrit le contact dans l'annuaire

		}
	}

}
