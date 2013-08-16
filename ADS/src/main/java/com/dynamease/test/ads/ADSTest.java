package com.dynamease.test.ads;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.ldap.model.schema.registries.SchemaLoader;
import org.apache.directory.api.ldap.schemaextractor.SchemaLdifExtractor;
import org.apache.directory.api.ldap.schemaextractor.impl.DefaultSchemaLdifExtractor;
import org.apache.directory.api.ldap.schemaloader.LdifSchemaLoader;
import org.apache.directory.api.ldap.schemamanager.impl.DefaultSchemaManager;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.api.schema.SchemaPartition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.core.partition.ldif.LdifPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;

public class ADSTest {

	/** The Factory */
	private DefaultDirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();

	/** The directory service */
	private DirectoryService service;

	/** The LDAP server */
	private LdapServer server;

	/**
	 * Add a new partition to the server
	 * 
	 * @param partitionId
	 *            The partition Id
	 * @param partitionDn
	 *            The partition DN
	 * @return The newly added partition
	 * @throws Exception
	 *             If the partition can't be added
	 */
	private Partition addPartition(String partitionId, Dn partitionDn, SchemaManager schemaManager) throws Exception {
		// Create a new partition named 'foo'.
		JdbmPartition partition = new JdbmPartition(schemaManager);
		partition.setId(partitionId);
		partition.setPartitionPath(new File(workDir, partitionId).toURI());
		partition.setSuffixDn(partitionDn);
		service.addPartition(partition);

		return partition;
	}

	/**
	 * Add a new set of index on the given attributes
	 * 
	 * @param partition
	 *            The partition on which we want to add index
	 * @param attrs
	 *            The list of attributes to index
	 */
	private void addIndex(Partition partition, String... attrs) {
		// Index some attributes on the apache partition
		HashSet<JdbmIndex<String, Entry>> indexedAttributes = new HashSet<JdbmIndex<String, Entry>>();

		for (String attribute : attrs) {
			indexedAttributes.add(new JdbmIndex<String, Entry>(attribute, false));
		}

		// ((JdbmPartition) partition).setIndexedAttributes(indexedAttributes);

	}

	/**
	 * initialize the schema manager and add the schema partition to diectory
	 * service
	 * 
	 * @throws Exception
	 *             if the schema LDIF files are not found on the classpath
	 */
	private SchemaManager initSchemaPartition() throws Exception {

		// Extract the schema on disk (a brand new one) and load the registries
		File schemaRepository = new File(workDir, "schema");
		SchemaLdifExtractor extractor = new DefaultSchemaLdifExtractor(workDir);
		extractor.extractOrCopy(true);

		SchemaLoader loader = new LdifSchemaLoader(schemaRepository);
		SchemaManager schemaManager = new DefaultSchemaManager(loader);
		service.setSchemaManager(schemaManager);

		// Init the LdifPartition
		LdifPartition ldifPartition = new LdifPartition(schemaManager);
		SchemaPartition schemaPartition = new SchemaPartition(schemaManager);
		schemaPartition.setWrappedPartition(ldifPartition);

		// We have to load the schema now, otherwise we won't be able
		// to initialize the Partitions, as we won't be able to parse
		// and normalize their suffix DN
		schemaManager.loadAllEnabled();

		schemaPartition.setSchemaManager(schemaManager);

		List<Throwable> errors = schemaManager.getErrors();

		if (errors.size() != 0) {
			throw new Exception("Schema load failed : " + errors);
		}

		return schemaManager;
	}

	/**
	 * Initialize the server. It creates the partition, adds the index, and
	 * injects the context entries for the created partitions.
	 * 
	 * @param workDir
	 *            the directory to be used for storing the data
	 * @throws Exception
	 *             if there were some problems while initializing the system
	 */
	// Initial Source code from depot

	// private void initDirectoryService(File workDir) throws Exception {
	// // Initialize the LDAP service
	// service = new DefaultDirectoryService();
	//
	// // first load the schema
	// SchemaManager schemaManager = initSchemaPartition();
	//
	// // then the system partition
	// // this is a MANDATORY partition
	// Partition systemPartition = addPartition("system", new
	// Dn(ServerDNConstants.SYSTEM_DN), schemaManager);
	// service.setSystemPartition(systemPartition);
	//
	// // Disable the ChangeLog system
	// service.getChangeLog().setEnabled(false);
	// service.setDenormalizeOpAttrsEnabled(true);
	//
	// // Now we can create as many partitions as we need
	// // Create some new partitions named 'foo', 'bar' and 'apache'.
	// Partition fooPartition = addPartition("foo", new Dn("dc=foo,dc=com"),
	// schemaManager);
	// Partition barPartition = addPartition("bar", new Dn("dc=bar,dc=com"),
	// schemaManager);
	// Partition apachePartition = addPartition("apache", new
	// Dn("dc=apache,dc=org"), schemaManager);
	//
	// // Index some attributes on the apache partition
	// addIndex(apachePartition, "objectClass", "ou", "uid");
	//
	// // And start the service
	// service.startup();
	//
	// // Inject the foo root entry if it does not already exist
	// try {
	// service.getAdminSession().lookup(fooPartition.getSuffixDn());
	// } catch (Exception lnnfe) {
	// Dn dnFoo = new Dn("dc=foo,dc=com");
	// Entry entryFoo = service.newEntry(dnFoo);
	// entryFoo.add("objectClass", "top", "domain", "extensibleObject");
	// entryFoo.add("dc", "foo");
	// service.getAdminSession().add(entryFoo);
	// }
	//
	// // Inject the bar root entry
	// try {
	// service.getAdminSession().lookup(barPartition.getSuffixDn());
	// } catch (Exception lnnfe) {
	// Dn dnBar = new Dn("dc=bar,dc=com");
	// Entry entryBar = service.newEntry(dnBar);
	// entryBar.add("objectClass", "top", "domain", "extensibleObject");
	// entryBar.add("dc", "bar");
	// service.getAdminSession().add(entryBar);
	// }
	//
	// // Inject the apache root entry
	// if (!service.getAdminSession().exists(apachePartition.getSuffixDn())) {
	// Dn dnApache = new Dn("dc=Apache,dc=Org");
	// Entry entryApache = service.newEntry(dnApache);
	// entryApache.add("objectClass", "top", "domain", "extensibleObject");
	// entryApache.add("dc", "Apache");
	// service.getAdminSession().add(entryApache);
	// }
	//
	// }

	// Essais de modification YNI
	private void initDirectoryService(File workDir) throws Exception {

		// Initialize the LDAP service
		factory.init("instanceYNI");
		service = factory.getDirectoryService();

		// first load the schema
		SchemaManager schemaManager = initSchemaPartition();

		// Disable the ChangeLog system
		service.getChangeLog().setEnabled(false);
		service.setDenormalizeOpAttrsEnabled(true);

		// Now we can create as many partitions as we need
		// Create some new partitions named 'foo', 'bar' and 'apache'.
		Partition fooPartition = addPartition("foo", new Dn("dc=foo,dc=com"), schemaManager);
		Partition barPartition = addPartition("bar", new Dn("dc=bar,dc=com"), schemaManager);
		Partition apachePartition = addPartition("apache", new Dn("dc=apache,dc=org"), schemaManager);

		// Index some attributes on the apache partition
		addIndex(apachePartition, "objectClass", "ou", "uid");

		// And start the service
		service.startup();

		// Inject the foo root entry if it does not already exist
		try {
			service.getAdminSession().lookup(fooPartition.getSuffixDn());
		} catch (Exception lnnfe) {
			Dn dnFoo = new Dn("dc=foo,dc=com");
			Entry entryFoo = service.newEntry(dnFoo);
			entryFoo.add("objectClass", "top", "domain", "extensibleObject");
			entryFoo.add("dc", "foo");
			service.getAdminSession().add(entryFoo);
		}

		// Inject the bar root entry
		try {
			service.getAdminSession().lookup(barPartition.getSuffixDn());
		} catch (Exception lnnfe) {
			Dn dnBar = new Dn("dc=bar,dc=com");
			Entry entryBar = service.newEntry(dnBar);
			entryBar.add("objectClass", "top", "domain", "extensibleObject");
			entryBar.add("dc", "bar");
			service.getAdminSession().add(entryBar);
		}

		// Inject the apache root entry
		if (!service.getAdminSession().exists(apachePartition.getSuffixDn())) {
			Dn dnApache = new Dn("dc=Apache,dc=Org");
			Entry entryApache = service.newEntry(dnApache);
			entryApache.add("objectClass", "top", "domain", "extensibleObject");
			entryApache.add("dc", "Apache");
			service.getAdminSession().add(entryApache);
		}

	}
	
	private void injectEntry (String dnAsString) throws Exception {
		Dn dn = new Dn("o="+dnAsString+",dc=Apache,dc=Org");
		Entry entrytoInject = service.newEntry(dn);
		entrytoInject.add("objectClass", "top","organization");
		entrytoInject.add("o",dnAsString);
		service.getAdminSession().add(entrytoInject);
	}

	/**
	 * Creates a new instance of EmbeddedADS. It initializes the directory
	 * service.
	 * 
	 * @throws Exception
	 *             If something went wrong
	 */
	public ADSTest(File workDir) throws Exception {
		initDirectoryService(workDir);

	}

	/**
	 * starts the LdapServer
	 * 
	 * @throws Exception
	 */
	public void initServer() throws Exception {
		server = new LdapServer();
		int serverPort = 10389;
		server.setTransports(new TcpTransport(serverPort));
		server.setDirectoryService(service);

	}

	/**
	 * Main class.
	 * 
	 * @param args
	 *            Not used.
	 */

	private static File workDir = new File("/tmp", "/server-work");

	public static void main(String[] args) {
		try {
			String Filename = System.getProperty("java.io.tmpdir") + "/server-work";
			File workDir = new File(Filename);
			workDir.mkdirs();

			// Create the server
			ADSTest ads = new ADSTest(workDir);

			// Read an entry
			Entry result = ads.service.getAdminSession().lookup(new Dn("dc=apache,dc=org"));

			// And print it if available
			System.out.println("Found entry : " + result);

			// Initialization of the ldapServer
			ads.initServer();

			// play with starting and stopping the server
			System.out.println("Salut les aminches!");
			Scanner sc = new Scanner(System.in);
			String commande;

			do {
				System.out.print("Commande(start, stop, inject ou quit)    ?   ");
				commande = sc.nextLine();

				switch (commande) {
				case "quit":
					break;
				case "start":
					try {
						ads.server.start();
						System.out.println("Serveur demarre");
					}
					catch (Exception e){
						System.out.println("Le serveur ne peut pas demarrer");
						e.printStackTrace();
					}
					break;
				case "stop":
					try {
						ads.server.stop();
						System.out.println("Serveur arrete");
					}
					catch (Exception e){
						System.out.println("Le serveur ne peut pas s'arreter");
						e.printStackTrace();
					}
					break;
				case "inject":
					System.out.println("Entree à ajouter");
					String dn = sc.nextLine();
					try {
						ads.injectEntry(dn);
						System.out.println("entree ajoutee");
					}
					catch (Exception e){
						System.out.println("ca merde");
						e.printStackTrace();
					}
					break;
				default:
					break;
				}

			} while (!(commande.equals("quit")));

			sc.close();
			System.out.println("Salut on se casse!");
		} catch (Exception e) {
			// Ok, we have something wrong going on ...
			e.printStackTrace();
		}
	}

}
