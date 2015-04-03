package wozza.io.users;

import java.util.ArrayList;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.Authentication;

import wozza.io.utils.AuthUtils;

public class UserResource extends Resource<User> {
	
	public UserResource(User content) {
		super(content, new ArrayList<Link>());
	}
	
	public static UserResource createFrom(User user, Authentication auth) {
		UserResource userResource = new UserResource(user);

		userResource.add(ControllerLinkBuilder
							.linkTo(ControllerLinkBuilder.methodOn(UserController.class)
									.getUser(auth, user.getUsername()))
								.withSelfRel());

		if (AuthUtils.hasAnyAuthority(auth, "COORD", "ADMIN")) {
			userResource.add(ControllerLinkBuilder
								.linkTo(ControllerLinkBuilder.methodOn(UserController.class)
										.setUserPassword(auth, user.getUsername(), null))
									.withRel("password"));
		}
		
		if (AuthUtils.hasAnyAuthority(auth, "ADMIN")) {
			userResource.add(ControllerLinkBuilder
								.linkTo(ControllerLinkBuilder.methodOn(UserController.class)
										.deleteUser(auth, user.getUsername()))
									.withRel("delete"));
		}

		return userResource;
	}
}

