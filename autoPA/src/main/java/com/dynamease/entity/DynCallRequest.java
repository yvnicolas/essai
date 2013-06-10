package com.dynamease.entity;

/** Request to parse into Json. (POJO)
 * @author Antoine
 */

public class DynCallRequest {
	private DynContact sender;
	private DynSubscriber receiver;

	public DynCallRequest(){
	}
	
	/**
	 * @param sender
	 * @param receiver
	 */
	public DynCallRequest(DynContact sender, DynSubscriber receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}
	/**
	 * @return the sender
	 */
	public DynContact getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(DynContact sender) {
		this.sender = sender;
	}
	/**
	 * @return the receiver
	 */
	public DynSubscriber getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(DynSubscriber receiver) {
		this.receiver = receiver;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\nDynCallRequest [\n\tsender = " + sender + ",\n\treceiver = " + receiver
				+ "\n]";
	}
}
