package com.dynamease.addressBooks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.linkedin.api.LinkedInConnections;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

//Todo : for the moment, supposes the connection has been established

public class DynLinkedInAddrBook extends DynExternalAddressBookImpl {

	@Autowired
	private LinkedInTemplate linkedIn;

	private List<LinkedInProfile> connections = linkedIn.connectionOperations().getConnections();
	private Iterator<LinkedInProfile> cursor = connections.iterator();
	private LinkedInProfile currentConnection = null;

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasNext() {

		return cursor.hasNext();
	}

	@Override
	public DynPerson getNextEntry() {
		if (cursor.hasNext()) {
			DynPerson toReturn = new DynPerson();
			currentConnection = cursor.next();
			toReturn.setFirstName(currentConnection.getFirstName());
			toReturn.setLastName(currentConnection.getLastName());
			return toReturn;
		}
		return null;
	}

	@Override
	public int rateCurrent(DynCategories cat, DynSubscriber sub) {

		switch (cat) {

		case FAMILY:
			// TODO : change to call to generic family testing
			// example return sub.familyMapper(currentConection.getFirstName(),
			// currentConnection.getLastName(), mode : qui correspondrait à
			// l'update ou non du dictionnaire)
			return 0;
		case PROFESSIONAL:
			// TODO : change to actuall mapping indice
			return 100;
		default:
			return 0;

		}

	}

	@Override
	public Set<String> retreiveCategories() {
		// At this time, linked in connection does not enable to retreive
		// categories list, returns an empty set
		return new HashSet<String>();
	}

	@Override
	public DynCatRatedPerson next() {
		// TODO Auto-generated method stub
		return null;
	}

}
