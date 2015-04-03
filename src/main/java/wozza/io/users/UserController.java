package wozza.io.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wozza.io.Application.InMemoryUserDetailsService;

@RestController
public class UserController {

	@Autowired
	InMemoryUserDetailsService users;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value="/users/{userId}", method={RequestMethod.GET})
	public ResponseEntity<UserResource> getUser(Authentication auth, 
									 			@PathVariable("userId") String userId) {
		
		User user = users.get(userId);
		if (user == null)
			return new ResponseEntity<UserResource>(HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<UserResource>(UserResource.createFrom(user, auth), HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/users/{userId}", method={RequestMethod.PUT})
	public ResponseEntity<?> updateUser(Authentication auth, 
										@PathVariable("userId") String userId,
										@RequestBody User updatedUser) {
		
		User user = users.get(updatedUser.getUsername());
		if (user == null)
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		
		user.setRoles(updatedUser.getRoles());
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/users/{userId}", method={RequestMethod.DELETE})
	public ResponseEntity<?> deleteUser(Authentication auth, 
										@PathVariable("userId") String userId) {
		
		User user = users.remove(userId);
		if (user == null)
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/users/{userId}/password", method={RequestMethod.PUT})
	public ResponseEntity<?> setUserPassword(Authentication auth, 
											 @PathVariable("userId") String userId,
											 @RequestBody PasswordDao passwordDao) {
		
		User user = users.get(userId);
		if (user == null)
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		
		user.setPassword(passwordEncoder.encode(passwordDao.getPassword()));
			
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
