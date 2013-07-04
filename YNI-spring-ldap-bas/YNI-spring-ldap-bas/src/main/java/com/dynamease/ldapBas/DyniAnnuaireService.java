/**
 * 
 */
package com.dynamease.ldapBas;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yves
 * 
 */
/**
 * @author yves
 * 
 */
@Service
public class DyniAnnuaireService implements DyniSubscriberDaoInterface {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Resource(name = "dynattributesmapper")
	private Properties dynLdapAttributes;

	public DyniAnnuaireService() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(DyniAnnuaireService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dynamease.ldapBas.DyniAnnuaireInterface#existsAsSubscriber(com.dynamease
	 * .ldapBas.DyniPersonInterface)
	 */
	@Override
	public boolean isPresent(DyniPerson person) {

		try {

			DyniSubscriber result = (DyniSubscriber) ldapTemplate.lookup(constructSubscriberDN(person), new SubscriberContextMapper());

			// result actually should never be null as if the subscriber is not
			// found an exception is raides
			if (result == null) {
				logger.debug(String.format(">>> retour result null du lookup mais sans exception"));
				return false;
			}

			else
				return true;
		}

		// Raised if no match found
		catch (NameNotFoundException e) {
			logger.info(String.format("LDAP NameNotFound exception raised in existsAsSubscriber : %s", person.buildFullName()), e);
			return false;

			// Raised in case the connection to the directory is not valid
		} catch (CommunicationException e) {
			logger.info(String.format("LDAP CommunicationException exception raised in existsAsSubscriber : %s", person.buildFullName()), e);
			return false;

		}

	}

	@Override
	public DyniSubscriber getSubscriber(DyniPerson person) {
		DyniSubscriber toReturn = null;
		String dn = constructSubscriberDN(person);
		try {
			toReturn = (DyniSubscriber) ldapTemplate.lookup(dn, getContextMapper(DyniSubscriber.class));
		} catch (NameNotFoundException e) {
			logger.info(String.format("LDAP CommunicationException exception raised in getSubscriber : %s", person.buildFullName()), e);
			return null;
		}
		return toReturn;
	}

	@Override
	public List<DyniSubscriber> getHomonyms(DyniPerson person) {
		// TODO Auto-generated method stub
		return null;
	}

	/**Used to verify possible inconsistencies in subscriber id
	 * @param subscriber
	 * @return null if no Dynsubscriber exists in the directory with the id from the subscriber passed in parameter
	 * @return the DynSubscriber in the directory matching the id of the one given as a parameter
	 */
	protected DyniSubscriber subIdAlreadyInUse(DyniSubscriber subscriber) {
		
		SearchControls searchControls = new SearchControls(SearchControls.ONELEVEL_SCOPE, 100, 10000, null, true, false);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(dynLdapAttributes.getProperty("subid"));
		sb.append("=");
		sb.append(subscriber.getSubscriberid());
		sb.append(")");
		String filter = sb.toString();
		
		List<?> searchResult = ldapTemplate.search("", filter, searchControls, getContextMapper(DyniSubscriber.class));
		if (searchResult.isEmpty()) {
			return null;
		}
		else {
			return (DyniSubscriber) searchResult.get(0);
		}
		
	}

	@Override
	public void create(DyniSubscriber subscriber) throws DynInvalidSubIdException {
		String dn = constructSubscriberDN(subscriber);

		// checks whether the id already exists
		if (subIdAlreadyInUse(subscriber)!=null) {
			throw new DynInvalidSubIdException();
		}

		// At this stage, subscriber is valid and can be added to the directory
		DirContextAdapter context = new DirContextAdapter(dn);
		subscriberMaptoContext(subscriber, context);
		ldapTemplate.bind(context);

	}

	@Override
	public void delete(DyniSubscriber subscriber) throws DynInvalidSubIdException {
		
		DyniSubscriber toDelete;
		
		// checks wether the Id exists and if not throw exception
		toDelete = subIdAlreadyInUse(subscriber);
		if (toDelete == null) {
			throw new DynInvalidSubIdException();
		}
		
		// checks that what was in the directory is the same as the one to delete
		if (!(subscriber.equals(toDelete))) {
			throw new DynInvalidSubIdException();
		}
		
		// Actually deletes the object in the directory
		ldapTemplate.unbind(constructSubscriberDN(subscriber));

	}

	@Override
	public void update(DyniSubscriber subscriber) throws DynInvalidSubIdException {
		
		DirContextOperations context;
		
		try {
			context = ldapTemplate.lookupContext(constructSubscriberDN(subscriber));
		}
		catch (NameNotFoundException e) {
			throw new DynInvalidSubIdException();
		}
				
		// checks that what was in the directory is the same as the one to update
		if (subscriber.getSubscriberid()!= Integer.parseInt(context.getStringAttribute(dynLdapAttributes.getProperty("subid")))) {
			throw new DynInvalidSubIdException();
		}
		
		// Actually updates the object in the directory
		subscriberMaptoContext(subscriber, context);
		ldapTemplate.modifyAttributes(context);

	}

	/*
	 * Tool classes and method to manage attributes mapping between objects and
	 * the LDAP directory.
	 * 
	 * Context mappers : used to get the data from directory to Java
	 */

	protected void personAttributesMapping(DyniPerson person, DirContextAdapter context) {
		person.setFirstName(context.getStringAttribute(dynLdapAttributes.getProperty("first")));
		person.setLastName(context.getStringAttribute(dynLdapAttributes.getProperty("last")));
	}

	private class PersonContextMapper implements ContextMapper {
		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			DyniPerson person = new DyniPerson();
			personAttributesMapping(person, context);
			return person;
		}
	}

	private class SubscriberContextMapper implements ContextMapper {
		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			DyniSubscriber result = new DyniSubscriber();
			personAttributesMapping(result, context);
			result.setSubscriberid(Integer.parseInt(context.getStringAttribute(dynLdapAttributes.getProperty("subid"))));
			return result;
		}
	}

	/**
	 * Finding the right context mapper based on the object looked at
	 * 
	 * TODO : should be externalized with the different context mappers as a
	 * service
	 * 
	 * @param c
	 *            : object class for which we want the context mapper to be
	 *            returned
	 * @return
	 */
	protected ContextMapper getContextMapper(Class<?> c) {
		if (c.equals(DyniSubscriber.class)) {
			return new SubscriberContextMapper();
		} else
			return new PersonContextMapper();
	}

	/*
	 * Tool classes and method to manage attributes mapping between objects and
	 * the LDAP directory.
	 * 
	 * Mappers : used to prepare the DirContextOperations before flushing it to
	 * LDAP directory
	 */

	/**
	 * Context basic instanciation valid for all person sub classes (subscriber
	 * or contact)
	 * 
	 * @param person
	 * @param context
	 */
	protected void basicPersonMaptoContext(DyniPerson person, DirContextOperations context) {

		context.setAttributeValues("objectclass", new String[] { "top", "person" });
		context.setAttributeValue(dynLdapAttributes.getProperty("full"), person.buildFullName());
		context.setAttributeValue(dynLdapAttributes.getProperty("last"), person.getLastName());
		context.setAttributeValue(dynLdapAttributes.getProperty("first"), person.getFirstName());

	}

	protected void subscriberMaptoContext(DyniSubscriber subscriber, DirContextOperations context) {
		basicPersonMaptoContext(subscriber, context);
		context.addAttributeValue("objectclass", dynLdapAttributes.getProperty("subscriberclass"));
		StringBuilder sb = new StringBuilder();
		sb.append(subscriber.getSubscriberid());
		context.setAttributeValue(dynLdapAttributes.getProperty("subid"), sb.toString());
	}

	/**
	 * Generates the theoretical DN of a subscriber passed as a DyniPerson in
	 * the parameter
	 * 
	 * @param subscriber
	 * @return
	 */
	private String constructSubscriberDN(DyniPerson subscriber) {
		return "cn= " + subscriber.buildFullName();
	}

}
