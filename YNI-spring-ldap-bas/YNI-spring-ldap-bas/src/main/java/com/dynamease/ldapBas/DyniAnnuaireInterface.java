/**
 * 
 */
package com.dynamease.ldapBas;

/**Interface for a Dynamease directory service
 * @author yves
 *
 */
public interface DyniAnnuaireInterface {
	
	/**Tells whether a person has subscribed to Dynamease
	 * @param person the person to be checked
	 * @return
	 */
	boolean existsAsSubscriber (DyniPerson person);
	
	/**Tells whether the contact is listed in the directory for the corresponding subscriber
	 * @param contact
	 * @param subscriber
	 * @return
	 */
	boolean existsAsContact (DyniPerson contact, DyniPerson subscriber);
	

}
