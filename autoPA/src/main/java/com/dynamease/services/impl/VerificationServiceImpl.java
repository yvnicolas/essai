package com.dynamease.services.impl;

import org.springframework.stereotype.Component;

import com.dynamease.entity.DynContact;
import com.dynamease.services.VerificationService;

@Component
public class VerificationServiceImpl implements VerificationService {

	private DynDirectory ldapDirectory = null;
	private boolean upAndRunning = false;

	public VerificationServiceImpl() {
		super();
		try {
			ldapDirectory = new DynDirectory();
			upAndRunning = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean verify(DynContact dynContact) {

		String nameToLookup = dynContact.getFirstName() + " "
				+ dynContact.getLastName();
		if (upAndRunning) {
			return ldapDirectory.nameLookup(nameToLookup);
		}
		else {
			return false;
		}
		}

	}

	/*
	 * Draft implementation through Spring Ldap Framework. Does not work for the
	 * moment. Kept it for further work
	 * 
	 * @Autowired private LdapTemplate annuaire;
	 * 
	 * 
	 * private class DynContactAttributesMapper implements AttributesMapper {
	 * public Object mapFromAttributes(Attributes attrs) throws NamingException
	 * { DynContact person = new DynContact();
	 * person.setFullName((String)attrs.get("cn").get());
	 * person.setLastName((String)attrs.get("sn").get()); return person; } }
	 * 
	 * /**Verifies whether a particular contact is in the directory
	 * 
	 * @param dynContact
	 * 
	 * @return true if present in directory, false otherwise
	 */
	/*
	 * @Override public boolean verify(DynContact dynContact) {
	 * 
	 * boolean etat= true; // Theoretical DN construction from information in
	 * dyncontacts dynContact lookupResult; String dn =
	 * dynContact.constructDn(); try { lookupResult = (DynContact)
	 * ldapTemplate.lookup(dn, new DynContactAttributesMapper()); } catch
	 * (NamingException e) { etat = false; }
	 * 
	 * return (etat);
	 * 
	 * } End of tentative implementaion through spring ldap
	 */

