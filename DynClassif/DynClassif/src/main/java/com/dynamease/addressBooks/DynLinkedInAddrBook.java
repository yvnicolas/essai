package com.dynamease.addressBooks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

//TODO : Unit Test and connection test with a real linked in

public class DynLinkedInAddrBook extends DynExternalAddressBookImpl {

    private LinkedIn linkedIn = null;
    private List<LinkedInProfile> connections;
    private Iterator<LinkedInProfile> cursor;
    private LinkedInProfile currentConnection = null;

    public DynLinkedInAddrBook(LinkedInTemplate linkedIn) throws DynInvalidAddrBookConnection {
        super();
        this.linkedIn = linkedIn;
        if (linkedIn != null) {
            this.connections = linkedIn.connectionOperations().getConnections();
            this.cursor = connections.iterator();
        } else
            throw new DynInvalidAddrBookConnection("Trying to create a DynLinkedInAddrBook with null linkedIn");
    }

    @Override
    public boolean hasNext() {
        if ((cursor == null) || (linkedIn == null))
            return false;
        else
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

}
