/**
 * 
 */
package com.dynamease.ldap;

import java.util.List;

/**
 * Interface for a Dynamease Subscriber DAO interface to the LDAP directory
 * 
 * @author Yves Nicolas
 * 
 */

public interface DyniSubscriberDaoInterface {

	/**
	 * Tells whether there is a Dynamease subscriber matching the person
	 * 
	 * @param person
	 *            the person to be checked
	 * @return true if there is at least one
	 * @return false if no one is found (including if the service can not
	 *         connect to the directory)
	 */
	boolean isPresent(DyniPerson person);

	/**
	 * Finds a subscriber matching the id given in
	 * parameters
	 * 
	 * @param id a unique Dynamease id Identifier
	 * @return the full Dynisubscriber with data found in the directory
	 * @return null if none is found
	 */
	DyniSubscriber getSubscriber(int id);

	/**
	 * Gets the list of all subscribers matching the person characteristics
	 * given in parameters
	 * 
	 * @param person
	 * @return the list of all homonyms matching with data found in the
	 *         directory
	 * @return null if none is found
	 */
	List<DyniSubscriber> getHomonyms(DyniPerson person);

	/**
	 * Creates the subscriber in the directory
	 * 
	 * @param subscriber
	 * @throws DynInvalidSubIdException
	 *             when a Subscriber with the same Id already exists in the
	 *             directory
	 */
	void create(DyniSubscriber subscriber) throws DynInvalidSubIdException;

	/**
	 * Delete the subscriber in the directory
	 * 
	 * @param subscriber
	 * @throws DynInvalidSubIdException
	 *             when a Subscriber doesnot exist or there is a mismatch
	 *             between the subscriber Id and the first and last name of the subscriber. No check is made for the other fields.
	 */
	void delete(DyniSubscriber subscriber) throws DynInvalidSubIdException;

	
	/**
	 * Update the subscriber in the directory with the data of the parameter
	 * 
	 * @param subscriber
	 * @throws DynInvalidSubIdException
	 *             when a Subscriber doesnot exist or there is a mismatch
	 *             between the subscriber Id and the full name of the subscriber
	 */
	void update(DyniSubscriber subscriber) throws DynInvalidSubIdException;

	

}
