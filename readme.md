#Test application FB signIn and other operations
##Documentations:
- [A Secondary Facebook Login with Spring Social](http://www.baeldung.com/facebook-authentication-with-spring-security-and-social)
- [Spring Social Facebook Integration Example](http://www.technicalkeeda.com/spring-tutorials/spring-social-facebook-integration-example)
- [Accessing Facebook Data](https://spring.io/guides/gs/accessing-facebook/)

## docs.spring.io
- [Spring Social Reference Manual](http://docs.spring.io/spring-social/docs/1.0.x/reference/html/index.html)
- - [Spring Social Facebook Reference Manual](http://docs.spring.io/spring-social-facebook/docs/1.0.x/reference/html/)
- [Spring Social Facebook Reference](http://docs.spring.io/spring-social-facebook/docs/current-SNAPSHOT/reference/htmlsingle/)

## Facebook Web SDKs JS (AngularJS)

- [Framework Guides for the JavaScript SDK](https://developers.facebook.com/docs/javascript/frameworks)
- - [Facebook SDK for JavaScript with AngularJS](https://developers.facebook.com/docs/javascript/howto/angularjs)


# Useful:
- [What can you do with PostgreSQL and JSON?](http://clarkdave.net/2013/06/what-can-you-do-with-postgresql-and-json/)
- [How to use Spring JPA with PostgreSQL | Spring Boot](http://javasampleapproach.com/spring-framework/use-spring-jpa-postgresql-spring-boot)



### Note:
- docker pull postgres
- docker run --name PostgreSQL_9.6 -p 5432:5432  -e POSTGRES_PASSWORD=b311 -e POSTGRES_USER=docker -e POSTGRES_DB=User -d postgres
- psql -p 5432 -U docker -W

- docker ps -a
- docker rm $(docker ps -a -q)
- docker images -a
- docker rmi $(docker images -a)