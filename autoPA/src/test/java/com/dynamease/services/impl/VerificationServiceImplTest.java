package com.dynamease.services.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dynamease.entity.DynContact;
import com.dynamease.entity.DynSubscriber;

public class VerificationServiceImplTest {

	static VerificationServiceImpl underTest = null;

	@Test
	public void testInit() {

		try {
			underTest = new VerificationServiceImpl();
			assertNotNull(underTest);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	
	@Test
	public void testNonExistingName() {
		DynContact contact;
		DynSubscriber d = new DynSubscriber();
		boolean result;
					
		contact = new DynContact("sldkfj", "sdlfkj", d);
		
		result = underTest.verify(contact);
		assertFalse(result);
		
	
	}
	
	@Test
	public void testExistingName() {
		DynContact contact;
		DynSubscriber d = new DynSubscriber();
		boolean result;
					
		contact = new DynContact("Yves", "Nicolas", d);
		result = underTest.verify(contact);
		assertTrue(result);
		
	
	}
}
