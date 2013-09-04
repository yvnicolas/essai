package com.dynamease.addressBooks;

/**Enumeration to describe types of categorization used by DynCategorizer
 * @author yves
 *
 */
public enum DynCatMode {

	FORCED, // Adds a specified category, same for all address book
	RESET,	// Same as forced but if contact is presents, erases all previously know category
	AUTO, 	// Should be used as default mode : adds the best matching category identified automatically
	LIGHTWEIGHT, // same as Auto but doesnt add the best category if its rating is not superior to a minimum level
	STRONGAUTO // same as Lightweight but if a category is matching, erases all previously existing categories in directory.

}
