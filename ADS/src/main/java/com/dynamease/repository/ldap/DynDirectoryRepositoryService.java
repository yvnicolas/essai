package com.dynamease.repository.ldap;

public interface DynDirectoryRepositoryService {

	public void startFromScratch() throws Exception;

	public void stop() throws Exception;
	
	public String info();

}
