package com.dynamease.addressBooks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Reference;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

//TODO : Unit Test and connection test with a real linked in

public class DynFacebookAddrBook extends DynExternalAddressBookImpl {

    // We keep the list of Friend IDs as connection
    private Facebook facebook = null;
    private List<String> connections;
    private Iterator<String> cursor;
    private String currentConnectionId = null;
    private FacebookProfile currentConnectionProfile = null;

    // Keeps the lists of the friend lists defined by the user.
    // This is initialised by the getFriendListinfo method.
    private List<Reference> friendLists;

    // Friends'list defined by the users to be kept for category operations
    // The key in the hashtable is the friend id and the value is the list of
    // reference to facebook custom defined friend lists this friend belongs to.
    // This is initialised by the getFriendListinfo method.
    private Hashtable<String, List<Reference>> fbFriendListMembership;

    public DynFacebookAddrBook(Facebook facebook) throws DynInvalidAddrBookConnection {
        super();
        this.facebook = facebook;
        if (facebook != null) {
            this.connections = facebook.friendOperations().getFriendIds();
            this.cursor = connections.iterator();
            getFriendListinfo();
        } else
            throw new DynInvalidAddrBookConnection("Trying to create a DynFacebookAddrBook with null facebook");
    }

    @Override
    public boolean hasNext() {
        if ((cursor == null) || (facebook == null))
            return false;
        else
            return cursor.hasNext();
    }

    @Override
    public DynPerson getNextEntry() {
        if (cursor.hasNext()) {
            DynPerson toReturn = new DynPerson();
            currentConnectionId = cursor.next();
            currentConnectionProfile = facebook.userOperations().getUserProfile(currentConnectionId);
            toReturn.setFirstName(currentConnectionProfile.getFirstName());
            toReturn.setLastName(currentConnectionProfile.getLastName());
            return toReturn;
        }
        return null;
    }

    // Facebook category Rating : we default a "good" rate to friend
    // otherwise we just look whether the user defined friends list map to
    // something.
    @Override
    public int rateCurrent(DynCategories cat, DynSubscriber sub) {

        // if the connection belongs to friends list, try to map these category
        // lists
        if (fbFriendListMembership.containsKey(currentConnectionId)) {
            for (Reference listRef : fbFriendListMembership.get(currentConnectionId)) {
                if (cat == categoryMapper.map(listRef.getId()))
                    return 100;
            }
        }
        // At this stage, if nothing has been found we give the default rating
        // for friend
        if (cat == DynCategories.FRIEND)
            return 60;
        else
            return 0;

    }

    /*
     * (non-Javadoc) Facebook friends lists are mapped to categories.
     * 
     * @see
     * com.dynamease.addressBooks.DynExternalAddressBookImpl#retreiveCategories
     * ()
     */
    @Override
    public Set<String> retreiveCategories() {
        HashSet<String> toReturn = new HashSet<String>();
        for (Reference ref : friendLists) {
            toReturn.add(ref.getName());
        }
        return toReturn;
    }

    /**
     * At initialisation, builds the map that will enable to tell for a friend
     * which friends lists he belongs to
     */
    private void getFriendListinfo() {
        fbFriendListMembership = new Hashtable<String, List<Reference>>();
        friendLists = facebook.friendOperations().getFriendLists();
        for (Reference listRef : friendLists) {

            // for each custom friend lists, we get the friends in this list
            List<Reference> members = facebook.friendOperations().getFriendListMembers(listRef.getId());

            // then for each member in this list, if he is not in the map, we
            // add it with the category. If he is already in the map, we add
            // this friend lists to the list of friends lists he belongs to.
            for (Reference memberRef : members) {
                if (fbFriendListMembership.containsKey(memberRef.getId())) {
                    List<Reference> memberCats = fbFriendListMembership.get(memberRef.getId());
                    memberCats.add(listRef);
                } else {
                    List<Reference> memberCats = new ArrayList<Reference>();
                    memberCats.add(listRef);
                    fbFriendListMembership.put(memberRef.getId(), memberCats);
                }
            }
        }
    }
}
