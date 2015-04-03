package wozza.io.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wozza.io.Application.InMemoryUserDetailsService;
import wozza.io.utils.AuthUtils;

@RestController
public class UsersController {

	@Autowired
	InMemoryUserDetailsService users;
	
	@RequestMapping(value="/users", method={RequestMethod.GET})
	public ResponseEntity<UsersResource> listUsers(Authentication auth) {
		
		List<UserResource> userDaos = new ArrayList<UserResource>();
		try {
			for (User user : users.values()) {
				userDaos.add(UserResource.createFrom(user, auth));
			}
		} catch(Exception e) {
			return new ResponseEntity<UsersResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		UsersResource resources = new UsersResource(userDaos);
		
		resources.add(ControllerLinkBuilder
						.linkTo(ControllerLinkBuilder.methodOn(UsersController.class).listUsers(auth))
							.withRel("self"));

		if (AuthUtils.hasAnyAuthority(auth, "ADMIN")) {
			resources.add(ControllerLinkBuilder
							.linkTo(ControllerLinkBuilder.methodOn(UsersController.class).createUser(null))
								.withRel("create"));
		}
		
		return new ResponseEntity<UsersResource>(resources, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users", method={RequestMethod.POST}, consumes="application/json")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		
		if (user.getUsername() == null) return new ResponseEntity<Object>(Collections.singletonMap("error", "missing username"), HttpStatus.BAD_REQUEST);
		
		try {
			if (users.get(user.getUsername()) != null)
				return new ResponseEntity<Object>(HttpStatus.CONFLICT);
				
				users.put(user.getUsername(), user);
			} catch(Exception e) {
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}
}
