package com.montilla.laboratory.myfeatures.common.configuration;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;


@Configuration
@EnableWebSecurity
@Order(1)
public class DigestSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private DigestAuthenticationEntryPoint getDigestEntryPoint() {
		DigestAuthenticationEntryPoint digestEntryPoint = new DigestAuthenticationEntryPoint();
		digestEntryPoint.setRealmName("admin-digest-realm");
		digestEntryPoint.setKey("somedigestkey");
		return digestEntryPoint;
	}

	private DigestAuthenticationFilter getDigestAuthFilter() throws Exception {
		DigestAuthenticationFilter digestFilter = new DigestAuthenticationFilter();
		digestFilter.setUserDetailsService(userDetailsServiceBean());
		digestFilter.setAuthenticationEntryPoint(getDigestEntryPoint());
		return digestFilter;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
        	.withUser("marcomarco")
            .password(passwordEncoder().encode("secret"))
            .roles("USER")
			.and()
			.withUser("admin")
			.password(passwordEncoder().encode("secret"))
			.roles("ADMIN");
	}

	@Bean
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
		//return new BCryptPasswordEncoder();
	}
	//UserDetailsServiceAutoConfiguration
	
	/*
	 * Observe that we are configuring /admin/** for the SecurityFilterChain configured because of this configuration file
	 * Observe how we are customizing the filter chain and adding DigestAuthenticationFilter
	 * Also observe how we are configuring authenticationEntryPoint
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/admin/**")
			.addFilter(getDigestAuthFilter())
			.exceptionHandling()
			.authenticationEntryPoint(getDigestEntryPoint())
			.and()
			.authorizeRequests()
			.antMatchers("/admin/**")
			.hasRole("ADMIN");
	}

}
