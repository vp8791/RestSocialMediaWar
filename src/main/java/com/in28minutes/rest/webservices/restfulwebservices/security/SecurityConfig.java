package com.in28minutes.rest.webservices.restfulwebservices.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//https://www.youtube.com/watch?v=4x-HgnJ31cg
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// Authentication : User --> Roles
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		// user1 with password secret1 and roles USER  				===================>	(user1 has roles USER)
		// admin1 with password secret1 and roles USER and ADMIN    ===================>	(admin1 has roles USER and Admin)
		System.out.println("=======================In Configure AuthenticationManagerBuilder===============" );
		auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance()).withUser("user1").password("secret1")
				.roles("USER").and().withUser("admin1").password("secret1")
				.roles("USER", "ADMIN");
	}

	// Authorization : Role -> Access
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("=======================In Configure HttpSecurity===============" );
		//Role--> Access
		//Anything has /** should have role of USER
		http.httpBasic().and().authorizeRequests().antMatchers("/**")
				.hasRole("USER").antMatchers("/**").hasRole("USER")
				.antMatchers("/**").hasRole("ADMIN").and().csrf().disable()
				.headers().frameOptions().disable();
	}

}
