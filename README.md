# Getting Started

#### Update gradle wrapper to min required
'''sh
./gradlew wrapper --gradle-version=4.10 --distribution-type=bin
'''

# Spring Security
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

## Authentication at Spring Security
The main _`strategy interface`_ for authentication is **AuthenticationManager** which only has one method:

```java
public interface AuthenticationManager {

  Authentication authenticate(Authentication authentication)
    throws AuthenticationException;

}
```
Represents the token for an authentication request or for an _`authenticated principal`_ once the request has been 
processed by the _**AuthenticationManager.authenticate(Authentication)**_ method.  
Once the request has been authenticated, the **Authentication** will usually be stored in a _`thread-local SecurityContext`_ 
managed by the **SecurityContextHolder** by the _`authentication mechanism which is being used`_. An explicit authentication 
can be achieved, without using one of Spring Security's authentication mechanisms, by creating an **Authentication** 
instance and using the code:  
```java
 SecurityContextHolder.getContext().setAuthentication(anAuthentication);
```

Note: that unless the **Authentication** has the authenticated property set to true, it will still be authenticated by 
any security interceptor (for method or web invocations) which encounters it. In most cases, the framework 
transparently takes care of managing the security context and authentication objects for you.

An **AuthenticationManager** can do one of 3 things in its authenticate() method:

  - return an **Authentication** (normally with authenticated=true) if it can verify that the input represents a _`valid principal`_.

  - throw an **AuthenticationException** if it believes that the input represents an invalid principal.

  - return null if it can’t decide.

**AuthenticationException** is a runtime exception. It is usually handled by an application in a generic way, depending
on the style or purpose of the application. In other words user code is not normally expected to catch and handle it.
For example, a web UI will render a page that says that the authentication failed, and a backend HTTP service
will send a 401 response, with or without a `WWW-Authenticate` header depending on the context.

The most commonly used implementation of **AuthenticationManager** is **ProviderManager**, which delegates to a chain of
**AuthenticationProvider** instances. 

**ProviderManager**: Iterates an **Authentication** request through a **List<AuthenticationProvider>**, these are 
usually tried in _`order`_ until one provides a non-null response. A non-null response indicates the provider had authority 
to decide on the authentication request and no further providers are tried. If a subsequent provider 
successfully authenticates the request, the earlier authentication exception is disregarded and the successful 
authentication will be used. If no subsequent provider provides a non-null response, or a new **AuthenticationException**, 
the last **AuthenticationException** received will be used. If no provider returns a non-null response, or indicates 
it can even process an **Authentication**, the **ProviderManager** will throw a **ProviderNotFoundException**.  

A parent **AuthenticationManager** can also be set, and this will also be tried if none of the configured providers can 
perform the authentication. This is intended to support namespace configuration options though and is not a feature 
that should normally be required.  

The exception to this process is when a provider throws an **AccountStatusException**, in which case no further 
providers in the list will be queried. Post-authentication, the credentials will be cleared from the returned 
**Authentication** object, if it implements the **CredentialsContainer** interface. This behaviour can be controlled by 
modifying the _`eraseCredentialsAfterAuthentication`_ property.

**Authentication** _`event publishing`_ is delegated to the configured **AuthenticationEventPublisher** which defaults to 
a null implementation which doesn't publish events, so if you are configuring the bean yourself you must inject a 
publisher bean if you want to receive events. The standard implementation is **DefaultAuthenticationEventPublisher** 
which maps common exceptions to events (in the case of authentication failure) and publishes an **AuthenticationSuccessEvent** 
if authentication succeeds. If you are using the namespace then an instance of this bean will be used automatically 
by the <http> configuration, so you will receive events from the web part of your application automatically.  

Note that the implementation also publishes authentication failure events when it obtains an authentication result 
(or an exception) from the "parent" **AuthenticationManager** if one has been set. So in this situation, the parent should 
not generally be configured to publish events or there will be duplicates.

An **AuthenticationProvider** Indicates a class can process a specific **Authentication** implementation, is a bit 
like an **AuthenticationManager** but it has an extra method to allow the caller to query if it supports 
a given **Authentication** type:

```java
public interface AuthenticationProvider {

	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;

	boolean supports(Class<?> authentication);

}
```

The **Class<?>** argument in the supports() method is really Class<? extends Authentication> (it will only ever be asked
if it supports something that will be passed into the authenticate() method). A **ProviderManager** can support multiple
different authentication mechanisms in the same application by delegating to a chain of AuthenticationProviders.
If a ProviderManager doesn’t recognise a particular Authentication instance type it will be skipped.

A **ProviderManager** has an optional parent, which it can consult if all providers return null. If the parent is
not available then a null **Authentication** results in an **AuthenticationException**.

Sometimes an application has logical groups of protected resources
(e.g. all web resources that match a path pattern /api/**), and each group can have its
own dedicated **AuthenticationManager**. Often, each of those is a **ProviderManager**, and they share a parent.
The parent is then a kind of "global" resource, acting as a fallback for all providers.

## Customizing Authentication Managers
Spring Security provides some configuration helpers to quickly _`get common authentication manager`_ features set up in
your application. The most commonly used helper is the **AuthenticationManagerBuilder** that is used to create an 
**AuthenticationManager**. Allows for easily building in memory authentication, LDAP authentication, JDBC based authentication, 
or for adding a custom **UserDetailsService**, which is core interface which loads user-specific data. It is used 
throughout the framework as a user DAO and is the strategy used by the **DaoAuthenticationProvider**.  

The interface requires only one read-only method, which simplifies support for new data-access strategies. 
Here’s an example of an application configuring the global (parent) **AuthenticationManager**:
```java
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

   ... // web stuff here

  @Autowired
  public void initialize(AuthenticationManagerBuilder builder, DataSource dataSource) {
    builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
      .password("secret").roles("USER");
  }

}
```
This example relates to a web application, but the usage of **AuthenticationManagerBuilder** is more widely applicable 
(see below for more detail on how web application security is implemented). Note that the **AuthenticationManagerBuilder** 
is **@Autowired** into a method in a **@Bean** - _`that is what makes it build the global (parent)`_ **AuthenticationManager**. 
In contrast if we had done it this way:
````java
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

  @Autowired
  DataSource dataSource;

   ... // web stuff here

  @Override
  public void configure(AuthenticationManagerBuilder builder) {
    builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
      .password("secret").roles("USER");
  }

}
````
(using an **@Override** of a method in the configurer) then the **AuthenticationManagerBuilder** _`is only used to build a "local"`_ 
**AuthenticationManager**, _`which is a child of the global one`_. In a Spring Boot application you can 
**@Autowired** the global one into another bean, but you can’t do that with the local one unless you explicitly expose it yourself.  

Spring Boot provides a default global **AuthenticationManager** (with just one user) unless you pre-empt it by providing 
your own bean of type **AuthenticationManager**. The default is secure enough on its own for you not to have to worry 
about it much, unless you actively need a custom global **AuthenticationManager**. If you do any configuration that 
builds an **AuthenticationManager** you can often do it locally to the resources that you are protecting and not worry 
about the global default.

# Web Security
Spring Security in the web tier (for UIs and HTTP back ends) _is based on Servlet Filters_, so it is helpful to look at
the role of Filters generally first.

The client sends a request to the app, and the container decides which filters and which servlet apply to it _`based on
the path of the request URI`_. At most one servlet can handle a single request, but _`filters form a chain`_, so they are
ordered, and in fact a filter can veto the rest of the chain if it wants to handle the request itself.

A filter can also modify the request and/or the response used in the downstream filters and servlet. _`The order of
the filter chain is very important`_, and Spring Boot manages it through 2 mechanisms:

  - The first one, is that **@Beans** of type Filter can have an **@Order** or implement **Ordered**.
  - The second one, is that they can be part of a **FilterRegistrationBean** that itself has an order as part of its API.  

Some off-the-shelf filters define their own constants to help signal what order they like to be in relative to each 
other (e.g. the **SessionRepositoryFilter** _from Spring Session_ has a DEFAULT_ORDER of Integer.MIN_VALUE + 50, 
which tells us it likes to be early in the chain, but it doesn’t rule out other filters coming before it).

## FilterChainProxy
Delegates Filter requests to a _`list of Spring-managed`_ filter beans. As of version 2.0, you shouldn't need to explicitly 
configure a **FilterChainProxy** bean in your application context unless you need very fine control over the filter chain contents. 
Most cases should be adequately covered by the default <security:http /> namespace configuration options.  

The **FilterChainProxy** is linked into the servlet container filter chain by adding a standard Spring **DelegatingFilterProxy** 
declaration in the application web.xml file.

### Configuration
As of version 3.1, **FilterChainProxy** is configured using a list of **SecurityFilterChain** instances, each of which 
contains a RequestMatcher and a list of filters which should be applied to matching requests. Most applications will 
only contain a single filter chain, and if you are using the namespace, you don't have to set the chains explicitly. 
If you require finer-grained control, you can make use of the <filter-chain> namespace element. This defines a URI 
pattern and the list of filters (as comma-separated bean names) which should be applied to requests which match the pattern. 
An example configuration might look like this:  
````xml
 <bean id="myfilterChainProxy" class="org.springframework.security.util.FilterChainProxy">
      <constructor-arg>
          <util:list>
              <security:filter-chain pattern="/do/not/filter*" filters="none"/>
              <security:filter-chain pattern="/**" filters="filter1,filter2,filter3"/>
          </util:list>
      </constructor-arg>
  </bean>
````
The names "filter1", "filter2", "filter3" should be the bean names of **Filter** instances defined in the application 
context. The order of the names defines the order in which the filters will be applied. As shown above, use of the 
value "none" for the "filters" can be used to exclude a request pattern from the security filter chain entirely. 
Please consult the security namespace schema file for a full list of available configuration options.  

### Request Handling
Each possible pattern that the **FilterChainProxy** should service must be entered. The first match for a given request 
will be used to define all of the Filters that apply to that request. This means you must put most specific matches at 
the top of the list, and ensure all Filters that should apply for a given matcher are entered against the respective entry. 
The **FilterChainProxy** will not iterate through the remainder of the map entries to locate additional Filters.  

**FilterChainProxy** respects normal handling of Filters that elect not to call:
```java
Filter.doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
```
in that the remainder of the original or FilterChainProxy-declared filter chain will not be called.

### Request Firewalling
An HttpFirewall instance is used to validate incoming requests and create a wrapped request which provides consistent 
path values for matching against. See **DefaultHttpFirewall**, for more information on the type of attacks which the 
default implementation protects against. A custom implementation can be injected to provide stricter control over the 
request contents or if an application needs to support certain types of request which are rejected by default.  

Note that this means that you must use the Spring Security filters in combination with a **FilterChainProxy** 
if you want this protection. Don't define them explicitly in your web.xml file.  

**FilterChainProxy** will use the firewall instance to obtain both request and response objects which will be fed down 
the filter chain, so it is also possible to use this functionality to control the functionality of the response. 
When the request has passed through the security filter chain, the reset method will be called. With the default 
implementation this means that the original values of servletPath and pathInfo will be returned thereafter, instead of 
the modified ones used for security pattern matching.  

Since this additional wrapping functionality is performed by the **FilterChainProxy**, we don't recommend that you use 
multiple instances in the same filter chain. It shouldn't be considered purely as a utility for wrapping filter beans 
in a single Filter instance.  

### Filter Lifecycle
Note the Filter lifecycle mismatch between the servlet container and IoC container. As described in the **DelegatingFilterProxy** 
Javadocs, we recommend you allow the IoC container to manage the lifecycle instead of the servlet container. 
**FilterChainProxy** does not invoke the standard filter lifecycle methods on any filter beans that you add to the application context.  

  
Spring Security is installed as a single Filter in the chain, and its concrete type is **FilterChainProxy**, for reasons 
that will become apparent soon. In a Spring Boot app the security filter is a @**Bean** in the **ApplicationContext**, and 
it is installed by default so that it is applied to every request. It is installed at a position defined by 
**SecurityProperties**.DEFAULT_FILTER_ORDER, which in turn is anchored by **FilterRegistrationBean**.REQUEST_WRAPPER_FILTER_MAX_ORDER 
(the maximum order that a Spring Boot app expects filters to have if they wrap the request, modifying its behaviour).  

In fact there is even one more layer of indirection in the security filter: it is usually installed in the container as 
a **DelegatingFilterProxy**, which does not have to be a Spring **@Bean**. The proxy delegates to a **FilterChainProxy** which is 
always a **@Bean**, usually with a fixed name of _`springSecurityFilterChain`_. It is the **FilterChainProxy** which contains all 
the security logic arranged internally as a chain (or chains) of filters. All the filters have the same API (they all 
implement the Filter interface from the Servlet Spec) and they all have the opportunity to veto the rest of the chain.  

There can be multiple filter chains all managed by Spring Security in the same top level **FilterChainProxy** and all unknown 
to the container. The Spring Security filter contains a list of filter chains, and dispatches a request to the first 
chain that matches it. The picture below shows the dispatch happening based on matching the request path 
(/foo/** matches before /**). This is very common but not the only way to match a request.  The most important 
feature of this dispatch process is that only one chain ever handles a request.

## DelegatingFilterProxy
Proxy for a standard Servlet 2.3 Filter, delegating to a Spring-managed bean that implements the Filter interface. 
Supports a "_`targetBeanName`_" filter init-param in web.xml, specifying the name of the target bean in the Spring application context.
web.xml will usually contain a **DelegatingFilterProxy** definition, with the specified filter-name corresponding to 
a bean name in Spring's root application context. All calls to the filter proxy will then be delegated to that bean in 
the Spring context, which is required to implement the standard Servlet 2.3 Filter interface.  

This approach is particularly useful for Filter implementation with complex setup needs, allowing to apply the full 
Spring bean definition machinery to Filter instances. Alternatively, consider standard Filter setup in combination with 
looking up service beans from the Spring root application context.  

NOTE: The lifecycle methods defined by the Servlet Filter interface will by default not be delegated to the target 
bean, relying on the Spring application context to manage the lifecycle of that bean. Specifying the "_`targetFilterLifecycle`_" 
filter init-param as "true" will enforce invocation of the Filter.init and Filter.destroy lifecycle methods on the 
target bean, letting the servlet container manage the filter lifecycle.  

As of Spring 3.1, **DelegatingFilterProxy** has been updated to optionally accept constructor parameters when using 
Servlet 3.0's instance-based filter registration methods, usually in conjunction with Spring 3.1's 
**WebApplicationInitializer** SPI. These constructors allow for providing the delegate Filter bean directly, or 
providing the application context and bean name to fetch, avoiding the need to look up the application context from the ServletContext.  

# Spring Security Laboratory
After a theoretical base, taking excerpts from websites, documentation of APIs, among others, we will proceed to carry out
our spring security laboratory, where, we will start from an API without security and step by step we will see how
the previous concepts are applied until the authentication is completed using Oauth 2.0.  

## Creating an Unsecured End Point
Before we can apply security to an application, is needed a single end point to secure.
First step in this branch is create a simple end point controller. Then we will secure it with Spring Security in the next step.  
```java
@RestController
public class HelloController {
	
	@GetMapping("/hello")
	public String sayHello(String name) {
		return "Hello "+name;
	}
	
	@GetMapping("/admin/hello")
	public String sayAdminHello(String name) {
		return "Hello Admin "+name;
	}

}
```
Open navigator and paste http://localhost:9090/hello?name=marcomarco we will see a hello message.

## Securing End Point
To prevent unauthorized users from call the rest endpoint /hello. As it is now, if someone get request to this path,
they see the greeting with no barriers to stop them. You need to add a barrier that forces the caller to sign in before
they can see that page.

Just configuring Spring Security in the application, if Spring Security is on the classpath, Spring Boot automatically secures
all HTTP endpoints with “basic” authentication. However, you can further customize the security settings. The first thing you
need to do is add Spring Security to the classpath.

With Gradle, you need to add two lines (one for the application and one for testing) in the dependencies closure in build.gradle,
as the following listing shows:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.security:spring-security-test'
```
At start time springboot application will log something like that:  

```sh
Using generated security password: 8fdbdc55-468e-4b52-97d4-3900fda55b8d

```
The current user password for automatic basic login. credentials are user:8fdbdc55-468e-4b52-97d4-3900fda55b8d

Try to access http://localhost:9090/hello?name=marcomarco
The server return _`http 302 code`_, that means, the target resource resides temporarily under a different URI.
Since the redirection might be altered on occasion,  the client ought to continue to use the effective request URI for future requests.

The server SHOULD generate a` _Location header field`_ in the response containing a URI reference for the different URI.
The user agent MAY use the Location field value for automatic redirection. The server's response payload usually contains a
short hypertext note with a hyperlink to the different URI(s), this is not the case.  

In this case _`http response header`_ will be _`Location: http://localhost:9090/login`_
Note: For historical reasons, a user agent MAY change the request method from POST to GET for the subsequent request.
If this behavior is undesired, the 307 Temporary Redirect status code can be used instead.


Try to login... success!!!

## Configure user and password

```yaml
spring:
  security:
    user:
      name: marco
      password: secret
```

Configure this properties in the application.yml and this gave you a basic authentication user:passsword form from springsecurity.  

Try to login... success!!!  

Up to this point, we have been observed how spring security is automatically configured by default, placing the minimum
security to our laboratory application. Basic authentication is only effective if applied in conjunction with TLS 2.0
until now.

## Spring Security Default Behavior 
In this section we will see how spring security works internally and how all the theoretical base is applied at
the filters, for that we will see how, an htttp request that enters our end point behaves and where.

   1.- The request is received firstly by the **DelegatingFilterProxy** this is a a standard Servlet Filter to delegating to
       a Spring-managed bean that implements the Filter interface **FilterChainProxy**.
   2.- The request is forwarded DelegatingFilterProxy.invoke -> FilterChainProxy[Default Chain: "any request".  
```json
      "Filter Chains": [
            [ "any request", [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@61840471,
                            org.springframework.security.web.context.SecurityContextPersistenceFilter@62ff049b,
                            org.springframework.security.web.header.HeaderWriterFilter@122bd271, 
                            org.springframework.security.web.csrf.CsrfFilter@36f4823d, 
                            org.springframework.security.web.authentication.logout.LogoutFilter@67069bc5, 
                            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@3bf81f91, 
                            org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@9312439, 
                            org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@677f4e40, 
                            org.springframework.security.web.authentication.www.BasicAuthenticationFilter@3dfea4e0, 
                            org.springframework.security.web.savedrequest.RequestCacheAwareFilter@10374f12, 
                            org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@4e12eaf9, 
                            org.springframework.security.web.authentication.AnonymousAuthenticationFilter@653802e7, 
                            org.springframework.security.web.session.SessionManagementFilter@1e43e2d7, 
                            org.springframework.security.web.access.ExceptionTranslationFilter@64f406c3, 
                            org.springframework.security.web.access.intercept.FilterSecurityInterceptor@72998353]]]]
```

**WebAsyncManagerIntegrationFilter**: Provides integration between the **SecurityContext** and Spring Web's **WebAsyncManager** by 
using the method:
SecurityContextCallableProcessingInterceptor.beforeConcurrentHandling(org.springframework.web.context.request.NativeWebRequest, Callable)
to populate the **SecurityContext** on the Callable. Filter base class that aims to _`guarantee a single execution per request`_ 
dispatch, on any servlet container. It provides  _`doFilterInternal()`_ method with **HttpServletRequest** and **HttpServletResponse** arguments.  
Subclasses may use _`isAsyncDispatch(HttpServletRequest)`_ to determine when a filter is invoked as part of an async dispatch, 
and use _`isAsyncStarted(HttpServletRequest)`_ to determine when the request has been placed in async mode and therefore 
the current dispatch won't be the last one for the given request. see more in API Docs.  

**SecurityContextPersistenceFilter**: Populates the **SecurityContextHolder** with information obtained from the configured
**SecurityContextRepository** prior to the request and stores it back in the repository once the request has completed and
clearing the context holder. By default it uses an **HttpSessionSecurityContextRepository**. See this class for information 
**HttpSession** related configuration options.  
This filter will only execute once per request, _`to resolve servlet container (specifically Weblogic) incompatibilities`_.  
This filter MUST be executed BEFORE any _`authentication processing mechanisms`_. Authentication processing mechanisms 
(e.g. BASIC, CAS processing filters etc) expect the **SecurityContextHolder** to contain a valid **SecurityContext** by the time they execute.  
This _`is essentially a refactoring`_ of the old **HttpSessionContextIntegrationFilter** to delegate the storage issues 
to a separate strategy, allowing for more customization in the way the security context is maintained between requests.
The _`forceEagerSessionCreation property`_ can be used to ensure that a session is always available before the filter 
chain executes (the default is false, as this is resource intensive and not recommended).

**HeaderWriterFilter** Filter implementation to add headers to the current request. Can be useful to add certain headers 
which enable browser protection. Like _`X-Frame-Options, X-XSS-Protection and X-Content-Type-Options`_.

**CsrfFilter** Applies Cross-site request forgery (CSRF) protection using a synchronizer token pattern.
Developers are required to ensure that CsrfFilter is invoked for any request that allows state to change. 
Typically this just means that they should ensure their web application follows proper REST semantics 
(i.e. do not change state with the HTTP methods GET, HEAD, TRACE, OPTIONS).  

Typically the CsrfTokenRepository implementation chooses to store the CsrfToken in HttpSession with HttpSessionCsrfTokenRepository. This is preferred to storing the token in a cookie which can be modified by a client application.

**OncePerRequestFilter** Filter base class that aims to guarantee a single execution per request dispatch, on any 
servlet container. It provides a _`doFilterInternal`_ method with **HttpServletRequest** and **HttpServletResponse** arguments.  
As of Servlet 3.0, a filter may be invoked as part of a javax.servlet.DispatcherType#REQUEST REQUESTor
javax.servlet.DispatcherType#ASYNC ASYNC dispatches that occur in separate threads. A filter can be configured in 
whether it should be involved in async dispatches. However, in some cases servlet containers assume different default 
configuration. Therefore sub-classes can override the method shouldNotFilterAsyncDispatch() to declare statically if 
they should indeed be invoked, once, during both types  of dispatches in order to provide thread initialization, 
logging, security, and so on. This mechanism complements and does not replace the need to configure a filter 
with dispatcher types.  see more in API Docs. 

**LogoutFilter** Logs a principal out. Polls a series of **LogoutHandlers**. The handlers should be specified in the order 
they are required. Generally you will want to call logout handlers **TokenBasedRememberMeServices** and **SecurityContextLogoutHandler** 
(in that order). After logout, a redirect will be performed to the URL determined by either the configured 
**LogoutSuccessHandler** or the **logoutSuccessUrl**, depending on which constructor was used.
  
  
**AbstractAuthenticationProcessingFilter** Abstract processor of browser-based HTTP-based authentication requests.  
###### Authentication Process
The filter requires that you set the _`authenticationManager property`_. An **AuthenticationManager** is required to process 
the _`authentication request tokens`_ created by implementing classes. This filter will intercept a request and attempt to 
perform authentication from that request if the request matches the _`setRequiresAuthenticationRequestMatcher(RequestMatcher)`_.  
**Authentication** is performed by the _`attemptAuthentication method`_, which must be implemented by subclasses.  
######  Authentication Success
If authentication is successful, the resulting **Authentication** object will _`be placed into`_ the **SecurityContext** for the 
_`current thread`_, which is guaranteed to have already been created by an earlier filter.  
The configured **AuthenticationSuccessHandler** will then be called to take the redirect to the appropriate destination 
after a successful login. The default behaviour is implemented in a **SavedRequestAwareAuthenticationSuccessHandler** 
which will make use of any **DefaultSavedRequest** set by the **ExceptionTranslationFilter** and redirect the user to 
the URL contained therein. Otherwise it will redirect to the webapp root "/". You can customize this behaviour by 
injecting a differently configured instance of this class, or by using a different implementation.  
See the _`successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)`_ method for more information.
######  Authentication Failure
If authentication fails, it will delegate to the configured **AuthenticationFailureHandler** to allow the failure information 
to be conveyed to the client. The default implementation is **SimpleUrlAuthenticationFailureHandler** , which sends a 401 
error code to the client. It may also be configured with a failure URL as an alternative. Again you can inject whatever 
behaviour you require here.  
######  Event Publication
If authentication is successful, an **InteractiveAuthenticationSuccessEvent** will be _`published via the application context`_. 
_`No events will be published if authentication was unsuccessful`_, because this would generally be recorded via an 
AuthenticationManager-specific application event.
######  Session Authentication
The class has an optional **SessionAuthenticationStrategy** which will be invoked immediately after a successful call to 
_`attemptAuthentication()`_. Different implementations can be injected to enable things like _`session-fixation attack_ `
prevention or to _`control the number of simultaneous sessions`_ a principal may have.  
  

**DefaultLoginPageGeneratingFilter**
For internal use with namespace configuration in the case where a user doesn't configure a login page. 
The configuration code will insert this filter in the chain instead. Will only work if a redirect is used to the login page.  

**OncePerRequestFilter** Filter base class that aims to guarantee a single execution per request dispatch, on any servlet 
container. It provides a doFilterInternal method with HttpServletRequest and HttpServletResponse arguments.  see more in API Docs.  

**RequestCacheAwareFilter** Responsible for reconstituting the saved request if one is cached and it _`matches the current request`_.  
It will call _`RequestCache#getMatchingRequest(HttpServletRequest, HttpServletResponse) getMatchingRequest`_ on the 
configured **RequestCache**. If the method returns a value (a wrapper of the saved request), it will pass this to the filter chain's
doFilter method. If null is returned by the cache, the original request is used and the filter has no effect.  

**SecurityContextHolderAwareRequestFilter** A **Filter** which populates the ServletRequest with a _`request wrapper which implements the 
servlet API security methods`_. **SecurityContextHolderAwareRequestWrapper** is extended to provide the following additional methods:  
**HttpServletRequest**#_`authenticate(HttpServletResponse)`_ - Allows the user to determine if they are authenticated and if not send the user 
to the login page. See #_`setAuthenticationEntryPoint(AuthenticationEntryPoint)`_.  
**HttpServletRequest**#_`login(String, String)`_ - Allows the user to authenticate using the **AuthenticationManager**. See #_`setAuthenticationManager(AuthenticationManager)`_.  
**HttpServletRequest**#_`logout()`_ - Allows the user to logout using the **LogoutHandler** configured in Spring Security. See _`#setLogoutHandlers(List)`_.  
**AsyncContext**#s*`tart(Runnable)`* - Automatically copy the **SecurityContext**  from the  **SecurityContextHolder**  found on the **Thread** that
 invoked **AsyncContext**#_`start(Runnable)`_ to the Thread that processes the Runnable.  
 
**AnonymousAuthenticationFilter** Detects if there is no **Authentication** object in the **SecurityContextHolder**, and populates it with one if needed.  

**SessionManagementFilter** Detects that a user has been authenticated since the start of the request and, if they
have, calls the configured SessionAuthenticationStrategy to perform any session-related activity such as activating 
session-fixation protection mechanisms or checking for multiple concurrent logins.

**ExceptionTranslationFilter** Handles any **AccessDeniedException** and **AuthenticationException** thrown within the filter chain.  
This filter is necessary because it _`provides the bridge between Java exceptions and HTTP responses`_. It is solely concerned 
with maintaining the user interface. This filter does not do any actual security enforcement.  
If an **AuthenticationException** is detected, the filter _`will launch the authenticationEntryPoint`_. This allows common handling 
of authentication failures originating from any subclass of` _org.springframework.security.access.intercept.AbstractSecurityInterceptor`_.  
If an **AccessDeniedException** is detected, the filter will determine whether or not the user is an anonymous user. 
If they are an anonymous user, _`the authenticationEntryPoint will be launched`_. If they are not an anonymous user, the 
filter will delegate to the _`org.springframework.security.web.access.AccessDeniedHandler`_. By default the filter will use 
_`org.springframework.security.web.access.AccessDeniedHandlerImpl`_.  
To use this filter, it is necessary to specify the following properties:  
   - _`authenticationEntryPoint`_ indicates the handler that _`should commence the authentication process`_ if 
an **AuthenticationException** is detected. Note that this may also _`switch the current protocol from http to https for an SSL`_ login.  
   - _`requestCache`_ determines the strategy _`used to save a request during the authentication process`_ in order 
   that it m*`ay be retrieved and reused once`* the user has authenticated. The default implementation is **HttpSessionRequestCache**.

**FilterSecurityInterceptor** Performs security handling of HTTP resources via a filter implementation.
**AbstractSecurityInterceptor** base class for **FilterSecurityInterceptor** Abstract class that implements security interception 
for secure objects. The **AbstractSecurityInterceptor** will _`ensure the proper startup configuration_ `of the security interceptor. 
It will also implement the proper handling of secure object invocations, namely:  
  - Obtain the **Authentication** object from the **SecurityContextHolder**.
  - Determine if the request relates to a _`secured or public invocation`_ by looking up the secure object request against the **SecurityMetadataSource**.
  - For an invocation that is secured (there is a list of **ConfigAttributes** for the secure object invocation):
      - If either the _`Authentication.isAuthenticated()`_ returns false, or the _`alwaysReauthenticate is true`_, authenticate 
        the request against the configured **AuthenticationManager**. When authenticated, replace the **Authentication** object 
        on the **SecurityContextHolder** with the returned value.
      - Authorize the request against the configured **AccessDecisionManager**.
      - Perform any run-as replacement via the configured **RunAsManager**.
      - Pass control back to the concrete subclass, which will actually proceed with executing the object. A **InterceptorStatusToken** 
        is returned so that after the subclass has finished proceeding with execution of the object, its finally clause can ensure 
        the **AbstractSecurityInterceptor** is re-called and tidies up correctly using _`finallyInvocation(InterceptorStatusToken)`_.
      - The concrete subclass will re-call the **AbstractSecurityInterceptor** via the _`afterInvocation(InterceptorStatusToken, Object)`_ method.
      - If the **RunAsManager** replaced the **Authentication** object, return the **SecurityContextHolder** to the 
        object that existed after the call to **AuthenticationManager**.
      - If an **AfterInvocationManager** is defined, invoke the invocation manager and allow it to replace the object 
        due to be returned to the caller.
  - For an invocation that is public (there are no **ConfigAttributes** for the secure object invocation):
      - As described above, the concrete subclass will be returned an **InterceptorStatusToken** which is subsequently 
        re-presented to the **AbstractSecurityInterceptor** after the secure object has been executed. 
        The **AbstractSecurityInterceptor** will take no further action when _`its afterInvocation(InterceptorStatusToken, Object)_ `is called.
  - Control again returns to the concrete subclass, along with the Object that should be returned to the caller. 
    The subclass will then return that result or exception to the original caller.

## Using a Credentials Database
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

