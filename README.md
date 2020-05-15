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

## Features
  - Comprehensive and extensible support for both Authentication and Authorization

  - Protection against attacks like session fixation, clickjacking, cross site request forgery, etc

  - Servlet API integration

  - Optional integration with Spring Web MVC

  - Much more…

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


