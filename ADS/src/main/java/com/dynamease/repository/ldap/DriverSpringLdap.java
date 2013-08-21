package com.dynamease.repository.ldap;

import java.util.HashSet;
import java.util.Set;

import org.apache.directory.server.core.schema.bootstrap.ApacheSchema;
import org.apache.directory.server.core.schema.bootstrap.ApachednsSchema;
import org.apache.directory.server.core.schema.bootstrap.AutofsSchema;
import org.apache.directory.server.core.schema.bootstrap.BootstrapSchema;
import org.apache.directory.server.core.schema.bootstrap.CollectiveSchema;
import org.apache.directory.server.core.schema.bootstrap.CorbaSchema;
import org.apache.directory.server.core.schema.bootstrap.CoreSchema;
import org.apache.directory.server.core.schema.bootstrap.CosineSchema;
import org.apache.directory.server.core.schema.bootstrap.DhcpSchema;
import org.apache.directory.server.core.schema.bootstrap.InetorgpersonSchema;
import org.apache.directory.server.core.schema.bootstrap.JavaSchema;
import org.apache.directory.server.core.schema.bootstrap.Krb5kdcSchema;
import org.apache.directory.server.core.schema.bootstrap.MozillaSchema;
import org.apache.directory.server.core.schema.bootstrap.NisSchema;
import org.apache.directory.server.core.schema.bootstrap.SambaSchema;
import org.apache.directory.server.core.schema.bootstrap.SystemSchema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.LdapTestUtils;

public class DriverSpringLdap {

	public static void main(String[] args) throws Exception {

		Set<BootstrapSchema> schemas = new HashSet<BootstrapSchema>();
		schemas.add(new ApachednsSchema());
		schemas.add(new ApacheSchema());
		schemas.add(new AutofsSchema());
		schemas.add(new CollectiveSchema());
		schemas.add(new CorbaSchema());
		schemas.add(new CoreSchema());
		schemas.add(new CosineSchema());
		schemas.add(new DhcpSchema());
		schemas.add(new InetorgpersonSchema());
		schemas.add(new JavaSchema());
		schemas.add(new Krb5kdcSchema());
		schemas.add(new MozillaSchema());
		schemas.add(new NisSchema());
		schemas.add(new SambaSchema());
		schemas.add(new SystemSchema());

		// DirContext dirContext =
		LdapTestUtils.startApacheDirectoryServer(10389, "dc=dynamease,dc=net", "defaultPartitionName", "uid=admin,ou=system", "secret", schemas);

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
