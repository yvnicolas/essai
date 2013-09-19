package com.dynamease.addressBooks;

import java.util.Set;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

public abstract class DynExternalAddressBookImpl implements DynExternalAddressBook {

    //TODO : changer le DynDefaultCategoryMapper en un bean spring @ bean
    DynCategoryMapper categoryMapper = new DynDefaultCategoryMapper();

    public abstract boolean hasNext();

    @Override
    public DynCatRatedPerson next(DynSubscriber sub) {

        // TODO : rajouter ici les traitements pre et post sur les données genre
        // famille...

        DynCatRatedPerson toReturn = new DynCatRatedPerson(this.getNextEntry());
        for (DynCategories cat : DynCategories.values()) {
            toReturn.setRate(cat, this.rateCurrent(cat, sub));
        }
        return toReturn;
    }

    @Override
    public void setCategoryMapper(DynCategoryMapper mapper) {
        this.categoryMapper = mapper;
    }

    @Override
    public abstract Set<String> retreiveCategories();

    /**
     * to Be implemented by each address book to generate the DynPerson
     * corresponding the the next connection from the information in the address
     * book
     * 
     * @return
     */
    public abstract DynPerson getNextEntry();

    /**
     * Used to get the rating note for a category concerning the current
     * connection
     * 
     * @param cat
     * @param sub
     * @return
     */
    public abstract int rateCurrent(DynCategories cat, DynSubscriber sub);

}
