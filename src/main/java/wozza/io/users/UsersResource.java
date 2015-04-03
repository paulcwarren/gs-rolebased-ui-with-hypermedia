package wozza.io.users;

import java.util.ArrayList;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

public class UsersResource extends Resources<UserResource> {
	
	public UsersResource(Iterable<UserResource> users) {
		super(users, new ArrayList<Link>());
	}
	
}
