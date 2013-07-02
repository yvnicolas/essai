/**
 * 
 */
package com.dynamease.ldapBas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextSource;
import org.springframework.ldap.core.support.DirContextSource;

/**
 * @author yves
 * 
 */
public class DyniAnnuaireService implements DyniAnnuaireInterface {


private AbstractContextSource ldapCtxt = null;;

private LdapTemplate ldapTemplate = null;

private static final String base = "dc=dynamease, dc=net";
private static final String bindUser = "uid=admin,ou=system";
private static final String bindpwd = "secret";
private static final String defaultUrl = "ldap://localhost:10389";



// Initialisation context source

private void initCtxt(String url) {
	ldapCtxt = new DirContextSource();
	ldapCtxt.setBase(base);
	ldapCtxt.setUserDn(bindUser);
	ldapCtxt.setPassword(bindpwd);
	ldapCtxt.setUrl(url);
	ldapTemplate = new LdapTemplate(ldapCtxt);
	
}



	public DyniAnnuaireService() {
	super();
	initCtxt(defaultUrl);
	}

	
	public DyniAnnuaireService(String url) {
	super();
	initCtxt(url);
	}

	private static final Logger logger = LoggerFactory
			.getLogger(DyniAnnuaireService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dynamease.ldapBas.DyniAnnuaireInterface#existsAsSubscriber(com.dynamease
	 * .ldapBas.DyniPersonInterface)
	 */
	@Override
	public boolean existsAsSubscriber(DyniPerson person) {
		try {
			DyniPerson result = (DyniPerson) ldapTemplate.lookup(
					constructSubscriberDN(person), new PersonContextMapper());
			if (result == null) {
				logger.debug(String
						.format(">>> retour result null du lookup mais sans exception"));
				return false;
			} else
				return true;
		}
		// In case a problem is returned by Ldap, this exception is raised
		// TODO check that lookup returns null if name is not found but
		// everything is ok
		// TODO In that case the naming exception should not be caught but
		// throwed upwards.
		catch (NamingException e) {
			logger.info(String.format(
					"LDAP exception raised in existsAsSubscriber : %s",
					e.getMessage()));
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dynamease.ldapBas.DyniAnnuaireInterface#existsAsContact(com.dynamease
	 * .ldapBas.DyniPersonInterface, com.dynamease.ldapBas.DyniPersonInterface)
	 */
	@Override
	public boolean existsAsContact(DyniPerson contact, DyniPerson subscriber) {
		// TODO Auto-generated method stub
		return false;
	}

	private class PersonContextMapper implements ContextMapper {
		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			DyniPerson person = new DyniPerson();

			person.setFirstName(context.getStringAttribute("givenName"));
			person.setLastName(context.getStringAttribute("sn"));
			return person;
		}
	}

	/**
	 * Generates the theoretical DN of a subscriber passed as a DyniPerson in
	 * the parameter
	 * 
	 * @param subscriber
	 * @return
	 */
	private String constructSubscriberDN(DyniPerson subscriber) {
		return "cn= " + subscriber.getFullName();
	}
}
