package com.dns.resttestbuilder.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//TODO
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	  private static final String[] AUTH_WHITELIST = {
	            // -- Swagger UI v2
	            "/v2/api-docs",
	            "/swagger-resources",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui.html",
	            "/webjars/**",
	            // -- Swagger UI v3 (OpenAPI)
	            "/v3/api-docs/**",
	            "/swagger-ui/**"
	    };

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
        .and().authorizeRequests()
        .antMatchers(AUTH_WHITELIST).permitAll()
        .antMatchers("/**").authenticated().and().oauth2ResourceServer().jwt();
	
	}
	

}
