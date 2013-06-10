package com.dynamease.web.rest;

import java.util.List;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dynamease.entity.DynSubscriber;

@Controller
public class LdapController {
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Autowired
	private DynSubscriber subscriber;

	public List<DynSubscriber> getAllUsers() {
		return ldapTemplate.search("", "objectclass=DynSubscriber",
				new userAttributesMapper());
	}

	public void addToLdapDirectory() {
		System.out.println("Trying to add " + subscriber.getFullName());
		if (userAlreadyExists())
			System.out.println("User already registered");
		// do nothing for now
		else {
			System.out.println("Adding " + subscriber.getFullName() + " to registry");
		}

	}
	
/*	@RequestMapping(value = "/ldap")
	@ResponseBody
	public String getLdap(){
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<< LDAP : " + ldapTemplate.toString());
		System.out.println(ldapController.getAllUsers().toString());
		
		ldapController.addToLdapDirectory();
		return "";
	}*/

	private boolean userAlreadyExists() {
		List<DynSubscriber> registeredUsers = getAllUsers();
		for (DynSubscriber tmpUser : registeredUsers) {
			if (tmpUser.getFullName().equals(subscriber.getFullName()))
				return true;
		}
		return false;
	}

	private class userAttributesMapper implements AttributesMapper {
		public Object mapFromAttributes(Attributes attrs)
				throws NamingException {
			DynSubscriber user = new DynSubscriber();
			try {
				user.setFirstName((String) attrs.get("cn").get());
				user.setLastName((String) attrs.get("cn").get());
			} catch (javax.naming.NamingException e) {
				e.printStackTrace();
			}
			return user;
		}
	}
}
