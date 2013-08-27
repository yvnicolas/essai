package com.dynamease.test.ads;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.api.ldap.model.schema.registries.Schema;
import org.apache.directory.api.ldap.schemamanager.impl.DefaultSchemaManager;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.api.schema.SchemaPartition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.avl.AvlPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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

		AvlPartition partition = new AvlPartition(schemaManager);
		partition.setId(partitionId);
		partition.setSuffixDn(partitionDn);
		service.addPartition(partition);

		return partition;
	}

	/**
	 * initialize the schema manager and add the schema partition to diectory
	 * service
	 * 
	 * @throws Exception
	 *             if the schema LDIF files are not found on the classpath
	 */
	private SchemaManager initSchemaPartition() throws Exception {

		SchemaManager schemaManager = new DefaultSchemaManager();
		service.setSchemaManager(schemaManager);

		Resource ldif = new ClassPathResource("dynSchemeV1.1.ldif");
		LdifFileLoader fileLoader = new LdifFileLoader(service.getAdminSession(), ldif.getFile().getAbsolutePath());
		fileLoader.execute();
	
		// Init the Partition
		AvlPartition partition = new AvlPartition(schemaManager);
		SchemaPartition schemaPartition = new SchemaPartition(schemaManager);
		schemaPartition.setWrappedPartition(partition);

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

	private void initDirectoryService() throws Exception {

		// Initialize the LDAP service
		factory.init("instanceYNI");
		service = factory.getDirectoryService();

		// first load the schema
		SchemaManager schemaManager = initSchemaPartition();
		service.setSchemaManager(schemaManager);

		// Disable the ChangeLog system
		service.getChangeLog().setEnabled(false);
		service.setDenormalizeOpAttrsEnabled(true);

		// Now we can create as many partitions as we need
		// Create some new partitions named 'foo', 'bar' and 'apache'.
		// Partition fooPartition = addPartition("foo", new Dn("dc=foo,dc=com"),
		// schemaManager);
		// Partition barPartition = addPartition("bar", new Dn("dc=bar,dc=com"),
		// schemaManager);
		Partition apachePartition = addPartition("dynamease", new Dn("dc=dynamease,dc=net"), schemaManager);

		// And start the service
		service.startup();


		// Inject the apache root entry
		if (!service.getAdminSession().exists(apachePartition.getSuffixDn())) {
			Dn dnApache = new Dn("dc=dynamease,dc=net");
			Entry entryApache = service.newEntry(dnApache);
			entryApache.add("objectClass", "top", "domain", "extensibleObject");
			entryApache.add("dc", "dynamease");
			service.getAdminSession().add(entryApache);
		}
		
		service.setAllowAnonymousAccess(true);

	}

	private void injectEntry(String dnAsString) throws Exception {
		Dn dn = new Dn("cn=" + dnAsString + ",dc=dynamease,dc=net");
		Entry entrytoInject = service.newEntry(dn);
		entrytoInject.add("objectClass", "top", "person");
		entrytoInject.add("cn", dnAsString);
		entrytoInject.add("sn", dnAsString);
		service.getAdminSession().add(entrytoInject);
	}

	/**
	 * Creates a new instance of EmbeddedADS. It initializes the directory
	 * service.
	 * 
	 * @throws Exception
	 *             If something went wrong
	 */
	public ADSTest() throws Exception {
		initDirectoryService();

	}

	/**
	 * starts the LdapServer
	 * 
	 * @throws Exception
	 */
	public void initServer() throws Exception {
		server = new LdapServer();
		int serverPort = 10391;
		server.setTransports(new TcpTransport(serverPort));
		server.setDirectoryService(service);

	}

	/**
	 * Loads an Ldif File
	 * 
	 * @throws IOException
	 * 
	 */
	public void loadFile(String fileName) throws IOException {

		Resource dirtest = new ClassPathResource(fileName);
		LdifFileLoader fileLoader = new LdifFileLoader(service.getAdminSession(), dirtest.getFile().getAbsolutePath());
		fileLoader.execute();

	}

	public static void main(String[] args) {
		try {

			// Create the server
			ADSTest ads = new ADSTest();

			// Read an entry
			Entry result = ads.service.getAdminSession().lookup(new Dn("dc=dynamease,dc=net"));

			// And print it if available
			System.out.println("Found entry : " + result);

			// Initialization of the ldapServer
			ads.initServer();

			// play with starting and stopping the server
			System.out.println("Salut les aminches!");
			Scanner sc = new Scanner(System.in);
			String commande;

			do {
				System.out.print("Commande(start, stop, inject, load ou quit)    ?   ");
				commande = sc.nextLine();

				switch (commande) {
				case "quit":
					break;
				case "start":
					try {

						ads.server.start();
						System.out.println("Liste des schémas enables");
						for (Schema s : ads.service.getSchemaManager().getEnabled()) {
							System.out.println(s.getSchemaName());
						}
						System.out.println("Liste des schémas disables");
						for (Schema s : ads.service.getSchemaManager().getDisabled()) {
							System.out.println(s.getSchemaName());
						}

					} catch (Exception e) {
						System.out.println("Le serveur ne peut pas demarrer");
						e.printStackTrace();
					}
					break;
				case "stop":
					try {
						ads.server.stop();
						System.out.println("Serveur arrete");
					} catch (Exception e) {
						System.out.println("Le serveur ne peut pas s'arreter");
						e.printStackTrace();
					}
					break;
				case "load":
					System.out.println("Fichier Ldif a charger");
					String file = sc.nextLine();
					try {
						ads.loadFile(file);
						System.out.println("Fichier charge");
					} catch (Exception e) {
						System.out.println("ca merde");
						e.printStackTrace();
					}
					break;
				case "inject":
					System.out.println("Entree � ajouter");
					String dn = sc.nextLine();
					try {
						ads.injectEntry(dn);
						System.out.println("entree ajoutee");
					} catch (Exception e) {
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