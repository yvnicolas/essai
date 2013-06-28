package com.dynamease.web.rest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CommunicationControllerTest {

	private CommunicationController comController = new CommunicationController();

	@Test
	public void communicateTest() {
		String jsonRequestWellFormed = "{\"receiver\":{\"firstName\":\"C\",\"lastName\":\"D\"}," +
							 "\"sender\":{\"firstName\":\"A\",\"lastName\":\"B\"}}";
		
		String expectedResponseWellFormed = "{ \"canContact\" : \"yes\", " +
									"\"communicationMean\" : \"phoneCall\", " +
									"\"address\" : \"9876543210\"}";
		
		String expectedResponseBadlyFormed = "{\"canContact\" : \"no \", " +
											"\"communicationMean\" : \"CorruptedJson\", " +
											"\"address\" : \"CorruptedJson\"}";
		//Assert.assertEquals(expectedResponse, comController.communicate(jsonRequestWellFormed));
		
		String jsonRequestBadlyFormed = "{\"firstName\" : \"A\", \"lastName\" : \"B\"}";
		Assert.assertEquals(expectedResponseBadlyFormed, comController.communicate(jsonRequestBadlyFormed));
	}
}
