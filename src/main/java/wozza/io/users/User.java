package wozza.io.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
	
	private String username;
	private String password;
	private Collection<String> roles;

	User(){}
	
	public User(org.springframework.security.core.userdetails.User user) {
		username = user.getUsername();
		roles = new ArrayList<String>();
		for (GrantedAuthority authority : user.getAuthorities()) {
			roles.add(authority.getAuthority());
		}
	}
	
	public User(String username, String password, List<String> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
}
