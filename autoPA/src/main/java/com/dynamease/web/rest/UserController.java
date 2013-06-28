package com.dynamease.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dynamease.entity.DynSubscriber;
import com.dynamease.entity.DynUser;

@Controller
@RequestMapping(value = "user")
public class UserController {

	@Autowired
	DynSubscriber subscriber;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DynUser get(@PathVariable("id") Long id) {

		DynUser user = new DynUser();
		user.setId(id);
		user.setName("nameValue");
		user.setLastname("lastnameValue");

		return user;
	}

	@RequestMapping(value = "/subscriber/{name}")
	@ResponseBody
	public DynSubscriber get(@PathVariable("name") String name){
		DynSubscriber user = new DynSubscriber();
		user.setLastName(name);
		user.setFirstName("toto");
		subscriber = user;
		System.out.println(subscriber.getLastName());
		return user;
	}

}
