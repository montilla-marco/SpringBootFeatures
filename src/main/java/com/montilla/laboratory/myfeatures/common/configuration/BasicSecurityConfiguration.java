package com.montilla.laboratory.myfeatures.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@Order(2)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

	//TODO: Extends Basic Authentication Sample
	/*@Autowired
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
 
		    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("user1").password(passwordEncoder().encode("user1Pass"))
          .authorities("ROLE_USER");
    }
 
		    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
          .antMatchers("/securityNone").permitAll()
          .anyRequest().authenticated()
          .and()
          .httpBasic()
          .authenticationEntryPoint(authenticationEntryPoint);
 
        http.addFilterAfter(new CustomFilter(),
				          BasicAuthenticationFilter.class);
    }

*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}

}
