/**
 * 
 */
package com.dynamease.addressBooks;

import com.dynamease.entity.DynCategories;

/**Interface that will enable mapping of specific address book categories to Dynamease categories
 * @author yves Nicolas
 *
 */
public interface DynCategoryMapper {
    
    /**Gets the DynCategorie matching the private internal address book category passed as argument.
     * @param internalCategory
     * @return null if no category can be mapped
     */
    public DynCategories map(String internalCategory);

}
