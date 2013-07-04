/**
 * 
 */
package com.dynamease.ldapBas;

/**
 * @author yves
 *
 */
public class DyniSubscriber extends DyniPerson {
	
	private int subscriberid;

	/**
	 * @param firstName
	 * @param lastName
	 */
	public DyniSubscriber(String firstName, String lastName) {
		super(firstName, lastName);
		}

	/**
	 * 
	 */
	public DyniSubscriber() {
		super();
	}

	public int getSubscriberid() {
		return subscriberid;
	}

	public void setSubscriberid(int subscriberid) {
		this.subscriberid = subscriberid;
	}

	@Override
	public String toString() {
		return String.format("DyniSubscriber [%s, subscriberid=%s]",super.toString(),subscriberid);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DyniSubscriber other = (DyniSubscriber) obj;
		if (subscriberid != other.subscriberid)
			return false;
		return true;
	}
	

}
