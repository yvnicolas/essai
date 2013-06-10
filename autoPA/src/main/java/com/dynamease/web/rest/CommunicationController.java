package com.dynamease.web.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import com.dynamease.entity.DynCallRequest;
import com.dynamease.entity.DynContact;
import com.dynamease.entity.DynSubscriber;
import com.dynamease.services.VerificationService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "communication")
public class CommunicationController {

	@Autowired
	private VerificationService verificationService;

	private ObjectMapper om = new ObjectMapper();
	
	@RequestMapping(headers = { "content-type=application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public String communicate(@RequestBody String ids) {
		
		//System.out.println("\nA new request! With args: " + ids);
		DynCallRequest request = null;
		String response = "";
		try{
			request = om.readValue(ids, DynCallRequest.class);
		}
		catch(IOException e){
			System.out.println("Json badly recognized");
			return "{\"canContact\" : \"no \", \"communicationMean\" : \"CorruptedJson\", \"address\" : \"CorruptedJson\"}";
		}
		
		DynSubscriber dynSubscriber = request.getReceiver();
		DynContact dynContact = request.getSender();
		dynContact.setRefDynSubscriber(dynSubscriber);
	
		if (verificationService.verify(dynContact)) {
			response = "{\"canContact\" : \"yes\", \"communicationMean\" : \"phoneCall\", \"address\" : \"9876543210\"}";
		} else {
			response = "{\"canContact\" : \"no\", \"communicationMean\" : \"\", \"address\" : \"\"}";
		}
		
		System.out.println("\nContact: "+dynContact+" \nWants to join: "+dynSubscriber+"\n\tAwnsering: "+response);
			
		return response;
	}
}
