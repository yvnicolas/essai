/**
 * 
 */
package com.dynamease.addressBooks;

import com.dynamease.entity.DynCategories;

/**Default simple implementation of CategoryMapper
 * checks for an exact match of the category name after normalizing to uppercase
 * @author yves
 *
 */
public final class DynDefaultCategoryMapper implements DynCategoryMapper {

    /**
     * 
     */
    public DynDefaultCategoryMapper() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.dynamease.addressBooks.DynCategoryMapper#map(java.lang.String)
     */
    @Override
    public DynCategories map(String internalCategory) {
        DynCategories toReturn=null;
        try {
            toReturn = DynCategories.valueOf(internalCategory.toUpperCase());
        }
       catch (IllegalArgumentException e) {}
        
        return toReturn;
         }

}
