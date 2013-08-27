package com.dynamease.test.ads;

/**Enables access to an inmemory Dynamease service directory for test purposes
 * @author yves
 *
 */
public interface EmbDynDir {

	
	/**
	 * Starts a void directory server on port.
	 * Directory is empty after startup but the dynamease schema has been loaded
	 * @param the port to start the server on
	 * @throws Exception 
	 */
	public void start(int port) throws Exception;
	
	/**Stops the embedded server
	 * All entries present at stopping time are lost.
	 * 
	 */
	public void stop();
	
	/**Loads the ldif file specified in the embedded directory.
	 * File name should be located on the class path
	 * Returns the number of entries added into the directory.
	 * @param fileName
	 * @return
	 */
	public int ldifLoad(String fileName);
}
