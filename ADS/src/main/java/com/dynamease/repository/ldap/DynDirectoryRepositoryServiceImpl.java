package com.dynamease.repository.ldap;

import java.util.HashSet;
import java.util.Set;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;

public class DynDirectoryRepositoryServiceImpl implements DynDirectoryRepositoryService {

	/** The Factory */
	private DefaultDirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();

	/** The directory service */
	private DirectoryService service;

	/** The LDAP server */
	private LdapServer server;

	// /**
	// * Add a new partition to the server
	// *
	// * @param partitionId
	// * The partition Id
	// * @param partitionDn
	// * The partition DN
	// * @return The newly added partition
	// * @throws Exception
	// * If the partition can't be added
	// */
	// private Partition addPartition(String partitionId, Dn partitionDn,
	// SchemaManager schemaManager) throws Exception {
	// // Create a new partition named 'foo'.
	// JdbmPartition partition = new JdbmPartition(schemaManager);
	// partition.setId(partitionId);
	// partition.setPartitionPath(new File(workDir, partitionId).toURI());
	// partition.setSuffixDn(partitionDn);
	// service.addPartition(partition);
	//
	// return partition;
	// }

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
	// private SchemaManager initSchemaPartition() throws Exception {
	//
	// // Extract the schema on disk (a brand new one) and load the registries
	// File schemaRepository = new File(workDir, "schema");
	// SchemaLdifExtractor extractor = new DefaultSchemaLdifExtractor(workDir);
	// extractor.extractOrCopy(true);
	//
	// SchemaLoader loader = new LdifSchemaLoader(schemaRepository);
	// SchemaManager schemaManager = new DefaultSchemaManager(loader);
	// service.setSchemaManager(schemaManager);
	//
	// // Init the LdifPartition
	// LdifPartition ldifPartition = new LdifPartition(schemaManager);
	// SchemaPartition schemaPartition = new SchemaPartition(schemaManager);
	// schemaPartition.setWrappedPartition(ldifPartition);
	//
	// // We have to load the schema now, otherwise we won't be able
	// // to initialize the Partitions, as we won't be able to parse
	// // and normalize their suffix DN
	// schemaManager.loadAllEnabled();
	//
	// schemaPartition.setSchemaManager(schemaManager);
	//
	// List<Throwable> errors = schemaManager.getErrors();
	//
	// if (errors.size() != 0) {
	// throw new Exception("Schema load failed : " + errors);
	// }
	//
	// return schemaManager;
	// }

	/**
	 * Initialize the server. It creates the partition, adds the index, and
	 * injects the context entries for the created partitions.
	 * 
	 * @param workDir
	 *            the directory to be used for storing the data
	 * @throws Exception
	 *             if there were some problems while initializing the system
	 */

	// Essais de modification YNI
	private void initDirectoryService() throws Exception {

		// Initialize the LDAP service
		factory.init("instanceYNI");
		service = factory.getDirectoryService();

		// first load the schema
		SchemaManager schemaManager = service.getSchemaManager();

	
		// Disable the ChangeLog system
		service.getChangeLog().setEnabled(false);
		service.setDenormalizeOpAttrsEnabled(true);

		// Now we can create as many partitions as we need
		// Create some new partitions named 'foo', 'bar' and 'apache'.
		Partition dynameasePartition = factory.getPartitionFactory().createPartition(schemaManager, "dynamease", "dc=dynamease,dc=net", 500, service.getInstanceLayout().getPartitionsDirectory());
		service.addPartition(dynameasePartition);

		// // Index some attributes on the apache partition
		// addIndex(apachePartition, "objectClass", "ou", "uid");

		// And start the service
		service.startup();

		// Inject the dynamease root entry
		try {
			service.getAdminSession().lookup(dynameasePartition.getSuffixDn());
		} catch (Exception lnnfe) {
			Dn dnDynamease = new Dn("dc=dynamease,dc=net");
			Entry entryBar = service.newEntry(dnDynamease);
			entryBar.add("objectClass", "top", "domain", "extensibleObject");
			entryBar.add("dc", "net");
			service.getAdminSession().add(entryBar);
		}

	}

	private void injectEntry(String dnAsString) throws Exception {
		Dn dn = new Dn("o=" + dnAsString + ",dc=Apache,dc=Org");
		Entry entrytoInject = service.newEntry(dn);
		entrytoInject.add("objectClass", "top", "organization");
		entrytoInject.add("o", dnAsString);
		service.getAdminSession().add(entrytoInject);
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

	@Override
	public void startFromScratch() throws Exception {

		// Init the service
		initDirectoryService();

		// Initialization of the ldapServer
		initServer();

	}

	@Override
	public void stop() throws Exception {
		server.stop();
		service.shutdown();

	}

	@Override
	public String info() {
		Set<Partition> listP = (Set<Partition>) service.getPartitions();
		StringBuilder sb = new StringBuilder(listP.size());
		sb.append(" Partitions définies : ");
		for (Partition P : listP) {
			sb.append(P.getId());
			sb.append("-");
		}
		return sb.toString();
	}

}
