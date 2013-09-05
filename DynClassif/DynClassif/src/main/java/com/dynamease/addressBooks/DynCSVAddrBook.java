package com.dynamease.addressBooks;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;
import com.dynamease.services.csv.impl.DynCSVServiceImpl;

public class DynCSVAddrBook extends DynExternalAddressBookImpl {

	
	private Logger logger = LoggerFactory.getLogger(DynCSVServiceImpl.class);
	
	
	
	public DynCSVAddrBook() {
		super();
		// TODO Auto-generated constructor stub
		// Passer le fichier en paramètre
		// Initialiser le BeanReader et normaliser le Header.
	}



	@Override
	public DynCatRatedPerson next() {
		// TODO Auto-generated method stub
		// Lecture du CSV Bean Reader et adaptation
		// Voir Fonction Process file
		return null;
	}

	

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		// jouer avec le beanreader null ou pas : un peu complexe
		return false;
	}

	@Override
	public Set<String> retreiveCategories() {
		// TODO Auto-generated method stub
		// Identifier le champ catégorie et prévoir un séparateur possible
		return null;
	}

	@Override
	public DynPerson getNextEntry() {
		// TODO Auto-generated method stub
		// Les bonnes colonnes du dynReader
		return null;
	}

	@Override
	public int rateCurrent(DynCategories cat, DynSubscriber sub) {
		// TODO Auto-generated method stub
		// A traiter : seulement s'ils ont les bonnes catégories.
		return 0;
	}



	@Override
	public void remove() {
		// TODO Auto-generated method stub
		// This is needed as DynExternal AddressBook extends iterator.
		// Not needed at the moment.
		
	}

}
