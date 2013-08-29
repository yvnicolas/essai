/**
 * 
 */
package com.dynamease.test.ads;

import java.io.IOException;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.ldap.schemamanager.impl.DefaultSchemaManager;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.avl.AvlPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author yves
 * 
 */
public class EmbDynDirImpl implements EmbDynDir {

	private static String INSTANCENAME = "instanceYNI";
	private static String PARTITIONID = "dynamease";
	private static String PARTITIONDN = "dc=dynamease,dc=net";
	private static String LDIFSCHEMEFILE = "dynSchemeV1.1.ldif";

	/** The Factory */
	private DefaultDirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();

	/** The directory service */
	private DirectoryService service;

	/** The LDAP server */
	private LdapServer server;
	
	

	public EmbDynDirImpl() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamease.test.ads.EmbDynDir#start(int)
	 */
	@Override
	public void start(int serverPort) throws Exception {

		// Initialize the LDAP service
		factory.init(INSTANCENAME);
		service = factory.getDirectoryService();
//		Dn partitionDn = new Dn(PARTITIONDN);
//
//		// first load the schema
		SchemaManager schemaManager = new DefaultSchemaManager();
		service.setSchemaManager(schemaManager);

		// Disable the ChangeLog system
		service.getChangeLog().setEnabled(false);
		service.setDenormalizeOpAttrsEnabled(true);
//
//		// Creating the Dynamease Partition
//		AvlPartition partition = new AvlPartition(schemaManager);
//		partition.setId(PARTITIONID);
//		partition.setSuffixDn(partitionDn);
//		service.addPartition(partition);

		// And start the service
		service.startup();
		
	
//		// Inject the dynamease root entry
//		if (!service.getAdminSession().exists(partition.getSuffixDn())) {
//			Entry rootEntry = service.newEntry(partitionDn);
//			rootEntry.add("objectClass", "top", "domain", "extensibleObject");
//			rootEntry.add("dc", PARTITIONID);
//			service.getAdminSession().add(rootEntry);
//			printinfo(service);
//		}
//
//		// Loads the Dynamease Ldap Schema
//
//		Resource ldif = new ClassPathResource(LDIFSCHEMEFILE);
//		LdifFileLoader fileLoader = new LdifFileLoader(service.getAdminSession(), ldif.getFile().getAbsolutePath());
//		fileLoader.execute();

		// Starts the ldap server
		server = new LdapServer();
		server.setTransports(new TcpTransport(serverPort));
		server.setDirectoryService(service);

	}

	private void printinfo(DirectoryService serv) {
		System.out.println(String.format("Infos sur le service %s", serv.getInstanceId()));
		System.out.println(String.format("Access Enabled : %s", serv.isAccessControlEnabled()));
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamease.test.ads.EmbDynDir#stop()
	 */
	@Override
	public void stop() {
		
		server.stop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamease.test.ads.EmbDynDir#ldifLoad(java.lang.String)
	 */
	@Override
	public int ldifLoad(String fileName) {
		
		int toReturn = 0;
		Resource dirtest = new ClassPathResource(fileName);
		LdifFileLoader fileLoader = null;
		try {
			fileLoader = new LdifFileLoader(service.getAdminSession(), dirtest.getFile().getAbsolutePath());
			toReturn = fileLoader.execute();
		} catch (IOException e) {
			toReturn = 0;
			e.printStackTrace();
		}
		
		return toReturn;
	}

}
