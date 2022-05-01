# User Profile CRUD - Sample Restful App

[![Build Status](https://drone.ujar.org/api/badges/ujar-org/basics-rest-user-profile-crud/status.svg?ref=refs/heads/main)](https://drone.ujar.org/ujar-org/basics-rest-user-profile-crud)
[![Quality Gate Status](https://sonarqube.ujar.org/api/project_badges/measure?project=ujar-org%3Abasics-rest-user-profile-crud&metric=alert_status&token=5a3db3103788bc4815b6cedc6897f9e437a76972)](https://sonarqube.ujar.org/dashboard?id=ujar-org%3Abasics-rest-user-profile-crud)

Minimal Spring Boot based RESTful CRUD example, which manages user profile entity (including Swagger UI).

## Technology stack

Java 17, Spring Boot

## Prerequisites

The following items should be installed in your system:

* Java 17 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE (IDEA preferably)

## Database configuration

Application uses a Postgres as a persistent storage. Options to get it up:

1. Use remote existing postgres. It is needed to specify the path in configs.
1. Start local postgres in docker `docker-compose -f docker-compose.dev.yml up -d`

### Running locally

This application is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built
using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:

```
git clone https://github.com/ujar-org/basics-rest-user-profile-crud.git
cd basics-rest-user-profile-crud
./mvnw package
java -jar target/*.jar
```

You can then access Swagger UI here: http://localhost:8080/swagger-ui.html

### Working with Application in your IDE

1) On the command line

```
git clone https://github.com/ujar-org/basics-rest-user-profile-crud.git
```

2) Inside IDE

In the main menu, choose `File -> Open` and select the Application [pom.xml](pom.xml). Click on the `Open` button.
Activate "local" profile in the Run settings or set it via environment
variables. [instruction](https://stackoverflow.com/questions/38520638/how-to-set-spring-profile-from-system-variable)
Wait to indexing completion and push the green "play" button.

3) Navigate to Swagger UI

Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) in your browser.

### Environment variables

It is required to pass env variables to the application to proper setup: (with default values)

## Code conventions

The code follows [Google Code Conventions](https://google.github.io/styleguide/javaguide.html) without exceptions. Code
quality is measured by:

- [PMD](https://pmd.github.io/)
- [CheckStyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)

## Tests

This project has standard JUnit tests. To run them execute this command:

```text
./mvnw test
```

It is mandatory to keep test code coverage not below **80** percents and cover all business logic and edge cases.

## Contributing

Contributing is available via pull requests.

Do your changes in your own branch from develop (we use [github flow](https://guides.github.com/introduction/flow)) and
send us pull request to develop. We check new PR's every weekday, so if you want to move faster feel free to contact
reviewers personally.

Code must be reviewed by at least 2 team members and all checks must be passed.
