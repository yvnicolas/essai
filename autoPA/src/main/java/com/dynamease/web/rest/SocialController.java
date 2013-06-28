package com.dynamease.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "social")
public class SocialController {

	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Autowired
	private LdapController ldapController;
	
//	@Autowired
//	ConnectionRepository connectionRepository;
//	
//	@RequestMapping(value = "/facebook")
//	@ResponseBody
//	public String  getInfos(){
//		Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
//		Facebook facebook = connection != null ? connection.getApi() : new FacebookTemplate();
//
//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< " + facebook.userOperations().getUserProfile().getLastName());
//		
//		List<FacebookProfile> contacts = facebook.friendOperations().getFriendProfiles();
//		for(FacebookProfile contact : contacts){
//			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< ami : " + contact.getLastName());
//		}
//		return "";
//	}

}
