package com.montilla.laboratory.myfeatures.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${user.oauth.clientId}")
    private String clientID;

    @Value("${user.oauth.clientSecret}")
    private String clientSecret;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()
                .withClient(clientID)
                .secret(passwordEncoder().encode(clientSecret))
                .scopes("user_info")
                .authorizedGrantTypes("authorization_code")
                .redirectUris("http://localhost:8082/montillalab/index.html")
                .autoApprove(false);
    }
        /*
        * Resource Owner Password Credentials Grant
        * The resource owner password credentials grant type supports situations where an
        *   application is trusted to handle end-user credentials and no other grant type is possible. For
            this grant type, the application collects the user’s credentials directly instead of redirecting
            the user to the authorization server. The application passes the collected credentials to the
            authorization server for validation as part of its request to get an access token.
            This grant type is discouraged because it exposes the user’s credentials to the
            application. It has been used for legacy embedded login pages and user migration
            scenarios. In either case, a vulnerability in the application can compromise the
            credentials. In addition, this grant type does not involve a user consent step, so an
            application can request any access it wishes using the user’s credentials. The user has no
            way to prevent abuse of their credentials.
            Consequently, this grant type is primarily recommended for user migration
            use cases. If users need to be migrated from one identity repository to another with
            incompatible password hashes, the new system can prompt a user for their credentials,
            use the resource owner password grant to validate them against the old system, and if
            valid, retrieve the user profile from the old system and store it and the credentials in the
            new system. This can avoid the necessity for large-scale forced password resets when
            migrating identity information. If this grant type is used, the client should throw away the
            user credentials as soon as it has obtained the access token, to reduce the possibility of
            compromised credentials.
        * */
//                .withClient("password_demo")
//                .secret(passwordEncoder().encode(clientSecret))
//                .scopes("resource:read")
//                .authorizedGrantTypes("password")
//                .redirectUris("http://localhost:9090/oauth/login/client-app")
//                .autoApprove(false);

                /*
                Client credentials grant ------------------------------------------------------------------------------------
                The client credentials grant type is used when an application calls an API to access
                resources the application owns. A quote is not owned by the individual user who needs the quote, so the
                call can be made on the application’s behalf. The application uses the client credentials
                grant type and authenticates to the authorization server with its own credentials to
                obtain an access token.*/
//                .withClient("client_credentials_demo")
//                .secret(passwordEncoder().encode(clientSecret))
//                .scopes("resource:read")
//                .authorizedGrantTypes("client_credentials")
//                .redirectUris("http://llocalhost:9090/oauth/login/client-app")
//                .autoApprove(false);


//        clients.inMemory()
//                .withClient("way2learnappclientid")
//                .authorizedGrantTypes("password","authorization_code")
//                .secret(passwordEncoder().encode("secret"))
//                .scopes("user_info","read","write")
//                .redirectUris("https://localhost:8443/myapp/login/oauth2/code/way2learnappclient")
//                .autoApprove(false)
//                .and()
//                .withClient("microclient")
//                .authorizedGrantTypes("password","authorization_code","client_credentials")
//                .secret(encoder().encode("secret"))
//                .scopes("user_info")
//                .redirectUris("https://localhost:8443/myapp/login/oauth2/code/way2learnappclient")
//                .autoApprove(false)

    // Resource Owner Password Credentials Grant
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
