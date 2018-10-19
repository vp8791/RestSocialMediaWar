package com.in28minutes.rest.webservices.restfulwebservices.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//https://www.youtube.com/watch?v=4x-HgnJ31cg
//https://www.boraji.com/spring-security-5-jdbc-based-authentication-example
//http://www.springboottutorial.com/securing-rest-services-with-spring-boot-starter-security
//C:\Users\Vanitha\Downloads\in28minutes.github.io-master\Website-SpringSecurityStarterWebApplication_Final (Delete at End)
//admin /admin@123 : http://localhost:9051/

//Password Generation
//String encoded=new BCryptPasswordEncoder().encode("admin@123");
// System.out.println(encoded);
//Login as admin/admin@123
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select USERNAME, PASSWORD, ENABLED" + " from LOGIN_USERS where USERNAME=?")
				.authoritiesByUsernameQuery("select USERNAME, AUTHORITY " + "from AUTHORITIES where USERNAME=?")
				.passwordEncoder(new BCryptPasswordEncoder()); 
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER").and().httpBasic(); // Authenticate users with
																								// HTTP basic
																								// authentication
	}

}
