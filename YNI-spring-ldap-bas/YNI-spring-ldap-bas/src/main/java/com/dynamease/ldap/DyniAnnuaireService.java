/**
 * 
 */
package com.dynamease.ldap;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Yves Nicolas
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


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dynamease.ldapBas.DyniAnnuaireInterface#existsAsSubscriber(com.dynamease
	 * .ldapBas.DyniPersonInterface)
	 */
	@Override
	public boolean isPresent(DyniPerson person) {
		if (getHomonyms(person).isEmpty()) {
			return false;
		} else
			return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DyniSubscriber> getHomonyms(DyniPerson person) {

		SearchControls searchControls = new SearchControls(SearchControls.ONELEVEL_SCOPE, 100, 10000, null, true, false);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(dynLdapAttributes.getProperty("full"));
		sb.append("=");
		sb.append(person.buildFullName());
		sb.append(")");
		String filter = sb.toString();

		return ldapTemplate.search("", filter, searchControls, getContextMapper(DyniSubscriber.class));

	}

	@Override
	public DyniSubscriber getSubscriber(int id) {

		SearchControls searchControls = new SearchControls(SearchControls.ONELEVEL_SCOPE, 100, 10000, null, true, false);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(dynLdapAttributes.getProperty("subid"));
		sb.append("=");
		sb.append(id);
		sb.append(")");
		String filter = sb.toString();

		List<?> searchResult = ldapTemplate.search("", filter, searchControls, getContextMapper(DyniSubscriber.class));
		if (searchResult.isEmpty()) {
			return null;
		} else {
			return (DyniSubscriber) searchResult.get(0);
		}

	}

	@Override
	public void create(DyniSubscriber subscriber) throws DynInvalidSubIdException {
		String dn = constructSubscriberDN(subscriber);

		// checks whether the id already exists
		if (getSubscriber(subscriber.getSubscriberid()) != null) {
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
		toDelete = getSubscriber(subscriber.getSubscriberid());
		if (toDelete == null) {
			throw new DynInvalidSubIdException();
		}

		// checks that what was in the directory is the same as the one to
		// delete
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
		} catch (NameNotFoundException e) {
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

	private String constructSubscriberDN(DyniSubscriber subscriber) {
		StringBuilder sb = new StringBuilder(dynLdapAttributes.getProperty("full"));
		sb.append("=");
		sb.append(subscriber.buildFullName());
		sb.append("+");
		sb.append(dynLdapAttributes.getProperty("subid"));
		sb.append("=");
		sb.append(subscriber.getSubscriberid());
		return sb.toString();

	}

}
