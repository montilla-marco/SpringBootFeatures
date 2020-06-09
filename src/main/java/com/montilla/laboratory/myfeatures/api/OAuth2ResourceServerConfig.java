package com.montilla.laboratory.myfeatures.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/oauth/authorize").permitAll()
                .anyRequest().authenticated();

//        http.authorizeRequests()
//            .antMatchers("/login").permitAll()
//            .antMatchers("/oauth/authorize")
//            .authenticated()
//            .and().formLogin()
//            .and().requestMatchers()
//            .antMatchers("/login", "/oauth/authorize");
    }

}
