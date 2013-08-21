package com.dynamease.repository.ldap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.LdapTestUtils;

public class DriverSpringLdap {

	public static void main(String[] args) throws Exception {

		// DirContext dirContext =
		LdapTestUtils.startApacheDirectoryServer(10389, "dc=dynamease,dc=net", "defaultPartitionName", "uid=admin,ou=system", "secret");

		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://127.0.0.1:10389");
		contextSource.setBase("dc=dynamease,dc=net");
		contextSource.setUserDn("uid=admin,ou=system");
		contextSource.setPassword("secret");
		contextSource.afterPropertiesSet();

		Resource ldif = new ClassPathResource("dynSchemeV1.1.ldif");
		LdapTestUtils.loadLdif(contextSource, ldif);

		System.out.println(">>>");

	}
}
