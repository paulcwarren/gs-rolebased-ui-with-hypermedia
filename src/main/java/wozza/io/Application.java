
package wozza.io;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wozza.io.site.SiteResource;
import wozza.io.users.User;
import wozza.io.users.UsersController;

@SpringBootApplication
@Controller
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping("/api")
	@ResponseBody
	public ResponseEntity<SiteResource> site(Authentication auth) {
		SiteResource site = new SiteResource();
		site.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(Application.class).site(auth)).withSelfRel());
		site.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UsersController.class).listUsers(auth)).withRel("users"));
		return new ResponseEntity<SiteResource>(site, HttpStatus.OK);
	}

	@RequestMapping("/user")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	    
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Bean
	    public PasswordEncoder passwordEncoder() {
	        return new StandardPasswordEncoder();
	    }
		
		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
				.formLogin()
					.and()
				.logout()
					.and()
	            .authorizeRequests()
	                .antMatchers("/", "/index.html", "/js/**", "/#/**").permitAll()
	                .antMatchers("/api").permitAll()
	                .antMatchers(HttpMethod.GET, "/users").permitAll()
	                .antMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
	                .antMatchers(HttpMethod.GET, "/user/**").permitAll()
	                .antMatchers(HttpMethod.DELETE, "/user/**").hasAuthority("ADMIN")
	                .antMatchers(HttpMethod.PUT, "/user/**/password").hasAnyAuthority("ADMIN", "COORD")
	                .anyRequest().authenticated()
	                .and()
	            .csrf()
	            	.disable();
	    }

	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	        	.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	    }
	}
	
	@Component("userDetailsService")
	public static class InMemoryUserDetailsService extends HashMap<String,User>
												   implements UserDetailsService {

		private static final long serialVersionUID = -5199683257736046334L;

		@Autowired
		PasswordEncoder encoder;
		
	    public InMemoryUserDetailsService() {
        	User defaultUser = new User("wozza", 
        								/*dotio*/ "19a2b2391971527ce2bee0e79b1fb79d30cb18e89c336baedbce02ad0fdc15d1dc8d1be8ad35e19d",
        								Collections.singletonList("ADMIN")); 
        	this.put(defaultUser.getUsername(), defaultUser);
	    }
	    
	    @Override
	    public UserDetails loadUserByUsername(final String login) {

	        String lowercaseLogin = login.toLowerCase();

	        User user = this.get(lowercaseLogin);

	        if (user == null) {
	            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found");
	        }

	        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	        for (String role : user.getRoles()) {
	        	authorities.add(new SimpleGrantedAuthority(role));
	        }
	        
	        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	    }
	}
}
