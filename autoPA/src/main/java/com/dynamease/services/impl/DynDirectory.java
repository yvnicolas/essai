package com.dynamease.services.impl;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

/**
 * Ldap Connexion for Dynamease directory
 * <p>
 * Uses apache ldap api
 * 
 * @author Yves Nicolas
 * 
 */
public class DynDirectory {

	/**
	 * Connexion annuaire sous-jacente...
	 */
	private LdapConnection annuaire = null;
	private String baseDN = "dc=dynamease,dc=net";
	private String adresseIP = "192.168.1.10";
	private String bindDN = "uid=admin,ou=system";
	private String pwd = "secret";

	private boolean initialise = false;

	public DynDirectory() throws Exception {
		InitConnexion();
	}

	private void connectIfNeeded() throws Exception {
		while (!initialise) {
			InitConnexion();
		}

	}

	/**
	 * 
	 * Ouvre une connexion annuaire sur la base des valeurs de constantes du
	 * package
	 * 
	 * @return rien du tout
	 */
	private void InitConnexion() throws Exception {

		annuaire = new LdapNetworkConnection(adresseIP, 10389);
		annuaire.bind(bindDN, pwd);
		initialise = true;

	}

	public boolean nameLookup(String nomCherche) {

		EntryCursor cursor = null;
		String filtre = "(cn=";
		filtre = filtre.concat(nomCherche);
		filtre = filtre.concat(")");
		try {
			connectIfNeeded();
		} catch (Exception e1) {
			return (false);
		}
		try {
			cursor = annuaire.search(baseDN, filtre, SearchScope.SUBTREE);
		} catch (LdapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			return (cursor.next());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
	}
}
