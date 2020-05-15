# Getting Started

## Update gradle wrapper to min required
'''sh
./gradlew wrapper --gradle-version=4.10 --distribution-type=bin
'''

### Spring Security
Spring Security is a powerful and highly customizable authentication and access-control framework.
It is the de-facto standard for securing Spring-based applications.

Spring Security is a framework that focuses on providing both authentication and authorization to Java applications.
Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements.

We take a look at the way security is applied in web applications using filters and more generally using method annotations.
Use this guide when you need to understand at a high level how a secure application works, and how it can be customized,
or if you just need to learn how to think about application security.

## Features
  - Comprehensive and extensible support for both Authentication and Authorization

  - Protection against attacks like session fixation, clickjacking, cross site request forgery, etc

  - Servlet API integration

  - Optional integration with Spring Web MVC

  - Much more…

## Authentication
The main strategy interface for authentication is AuthenticationManager which only has one method:

```java
public interface AuthenticationManager {

  Authentication authenticate(Authentication authentication)
    throws AuthenticationException;

}
```

An AuthenticationManager can do one of 3 things in its authenticate() method:

  - return an Authentication (normally with authenticated=true) if it can verify that the input represents a valid principal.

  - throw an AuthenticationException if it believes that the input represents an invalid principal.

  - return null if it can’t decide.

AuthenticationException is a runtime exception. It is usually handled by an application in a generic way, depending
on the style or purpose of the application. In other words user code is not normally expected to catch and handle it.
For example, a web UI will render a page that says that the authentication failed, and a backend HTTP service
will send a 401 response, with or without a WWW-Authenticate header depending on the context.

The most commonly used implementation of AuthenticationManager is ProviderManager, which delegates to a chain of
AuthenticationProvider instances. An AuthenticationProvider is a bit like an AuthenticationManager but it has an
extra method to allow the caller to query if it supports a given Authentication type:

```java
public interface AuthenticationProvider {

	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;

	boolean supports(Class<?> authentication);

}
```

The Class<?> argument in the supports() method is really Class<? extends Authentication> (it will only ever be asked
if it supports something that will be passed into the authenticate() method). A ProviderManager can support multiple
different authentication mechanisms in the same application by delegating to a chain of AuthenticationProviders.
If a ProviderManager doesn’t recognise a particular Authentication instance type it will be skipped.

A ProviderManager has an optional parent, which it can consult if all providers return null. If the parent is
not available then a null Authentication results in an AuthenticationException.

Sometimes an application has logical groups of protected resources
(e.g. all web resources that match a path pattern /api/**), and each group can have its
own dedicated AuthenticationManager. Often, each of those is a ProviderManager, and they share a parent.
The parent is then a kind of "global" resource, acting as a fallback for all providers.

## Creating an Unsecured End Point
Before we can apply security to an application, is needed a single end point to secure.
First step in this branch is create a simple end point controller. Then we will secure it with Spring Security in the next step.

## Securing End Point
To prevent unauthorized users from call the rest endpoint /hello. As it is now, if someone get request to this path,
they see the greeting with no barriers to stop them. You need to add a barrier that forces the caller to sign in before
they can see that page.

Just configuring Spring Security in the application. If Spring Security is on the classpath, Spring Boot automatically secures
all HTTP endpoints with “basic” authentication. However, you can further customize the security settings. The first thing you
need to do is add Spring Security to the classpath.

With Gradle, you need to add two lines (one for the application and one for testing) in the dependencies closure in build.gradle,
as the following listing shows:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.security:spring-security-test'
```
Try to access http://localhost:8080/hello?name=marcomarco
The server return http 302 code, that means, the target resource resides temporarily under a different URI.
Since the redirection might be altered on occasion,  the client ought to continue to use the effective request URI for future requests.

The server SHOULD generate a Location header field in the response containing a URI reference for the different URI.
The user agent MAY use the Location field value for automatic redirection. The server's response payload usually contains a
short hypertext note with a hyperlink to the different URI(s).

In this case http response header will be Location: http://localhost:8080/login
Note: For historical reasons, a user agent MAY change the request method from POST to GET for the subsequent request.
If this behavior is undesired, the 307 Temporary Redirect status code can be used instead.

At start time springboot application will log:
```sh
Using generated security password: 8fdbdc55-468e-4b52-97d4-3900fda55b8d

```
The current user password for automatic basic login.

## Configure user and password

```yaml
spring:
  security:
    user:
      name: marco
      password: secret
```

Configure this properties in the application.yml and this gave you a basic authentication user:passsword form from springsecurity


```java
@Configuration
@EnableWebSecurity
@Order(2)
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()
				.and() .httpBasic();
	}

}
```

Applying this Configuration Class. The endpoint will be prompt a basic authentication modal provided by navigator.

## Digest Authentication
Digest access authentication is one of the agreed-upon methods a web server can use to negotiate credentials, such as
username or password, with a user's web browser. This can be used to confirm the identity of a user before sending
sensitive information, such as online banking transaction history. It applies a hash function to the username and
password before sending them over the network. In contrast, basic access authentication uses the easily reversible
Base64 encoding instead of hashing, making it non-secure unless used in conjunction with TLS.

Technically, digest authentication is an application of MD5 cryptographic hashing with usage of nonce values to prevent
replay attacks. It uses the HTTP protocol.

Spring Security does have full out of the box support for the Digest authentication mechanism, this support is not as well
integrated into the namespace as Basic Authentication.


This example transaction consists of the following steps:

  - The client asks for http://localhost:9090/admin/hello?name=marcomarco that requires authentication but does not provide
a username and password. Typically this is because the user simply entered the address or followed a link to the page.

  - The server responds with the 401 "Unauthorized" response code, providing the authentication realm and a randomly generated,
single-use value called a nonce.
WWW-Authenticate: Digest realm="admin-digest-realm", qop="auth", nonce="MTU4OTU1MDc0MTg0OTpkM2Q4YWVmZjBjYmNkZDVmMWIxODIzNDhjZGExN2JjNw=="

  - At this point, the browser will present the authentication realm (typically a description of the computer or system being accessed)
to the user and prompt for a username and password. The user may decide to cancel at this point.

  - Once a username=admini and password=secret have been supplied, the client re-sends the same request but adds an authentication
   header that includes the response code.

  - In this example, the server accepts the authentication and the page is returned. If the username is invalid and/or
 the password is incorrect, the server might return the "401" response code and the client would prompt the user again.

