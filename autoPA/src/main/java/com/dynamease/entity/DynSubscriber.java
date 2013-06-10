/**
 * 
 */
package com.dynamease.entity;

import org.springframework.stereotype.Component;

/**
 * A subscriber to the Dynamease services. Should be referenced in the LDAP
 * directory
 * 
 * @author yves
 * 
 */
@Component
public class DynSubscriber extends DynPerson {

	private String fullName= "Yves Nicolas";  
	private int subscriberId = 123456;

	public DynSubscriber(){	
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return (false);
		} else { 
			return (!(this.subscriberId != ((DynSubscriber)obj).subscriberId));
		}
	}

	@Override
	public String toString() {
		
		String resultat = "DynSubscriber : ";
		resultat = resultat.concat(this.firstName);
		resultat = resultat.concat(" ");
		resultat = resultat.concat(this.lastName);
		return (resultat);
	}

}
