# Roles API

## Summary

This project is a Java SpringBoot microservice that spins up a REST API, called Roles API, to improve upon the
capabilities of the Users API and the Teams API.

# How to run

## Docker

**Requirements**

- docker
- docker-compose
- maven

### There are three ways to run this application
1. The solution can be executed with Docker and docker-compose. Since the build process needs to download all Maven
dependencies it can take some time to start.

```shell
docker-compose up --build
```

This will execute tests, build the jar, build the docker image and start the container. The first execution will take
longer since all maven dependencies will be downloaded. But next executions will run significantly faster as
dependencies are downloaded in a separate layer, thus cached. The `--build` triggers the Docker image build if there is
any change in the source code.

2. Using docker-compose that will create a container.
   After unzipping, Navigate inside the project directory and run the bash script file in the base directory.
```shell
start.sh
```
   This will run all the tests, create a docker image of the project and create a container using this image.

3. Run it from an IDE like eclipse or IntelliJ once the project has been imported and built using maven
## Build from source

**Requirements**

- maven
- java 11

To `build`, `test`, `package`, `report` and `run` execute the command:

```shell
mvn clean package verify spring-boot:run
```

To check the coverage open `target/site/jacoco/index.html`


# How to develop

## Spotless

To check for any code style issue

```shell
mvn spotless:check
```

To apply the fixes for code style issues

```shell
mvn spotless:apply
```

### Changes made in the PR:

- Singular naming convention for classes
- Changed method names to match camel case
- Removed hardcoded string values(status codes, json strings)
- In MockUtils made objectMapper instantiation as static
- Removed @Autowired Annotation from Service Impls as constructor with arguments will do the
needful
- Changed @OneToOne mapping to @ManyToOne mapping in Membership domain object as one Role can be associated with many memberships.
- Added missing tests in TeamService
- Added missing tests for TeamApi and UserApi
- Corrected Request Methods in Rest Controllers, used GetMapping for request methods incorrectly used as POST

#### MembershipRestController
- Added missing check for  whether team is present before creating memberships
- Added missing check for whether user belongs to the team with which membership is to be created.
- Changed assignRoleToMembership to return 201 as we are creating a new membership for the role and request mapping to PUT
- Changed getMemberships request handler method to get mapping from post mapping and method name to getByRole


#### RoleRestController
- Moved request parameter validations to the controller methods from service methods
- Added new api to get roles by userId and team Id

#### UserRestController and TeamResController
- Changed post mapping to get mapping for method handlers that do not consume any request but produce responses
- Added Api Tests using restAssured


#### MembershipService:
- Changed the method paramter name
- Added missing validations to assignRoleToMembership method
- Added missing tests to assignRoleToMembership method such check if role exists or user exists or team exists


#### RoleService:
- removed unwanted membershipService instance
- added new api to get roles By TeamId And UserId

#### Approach and thought process:
- Since each user or a team member that belongs to the given team has only ONE role assigned to the membership as we saw this behaviour from the membershipApi, the assumption was that client should be able to filter out roles that are assigned to members who are users of the same team or different team.
- In another words, for a given user Id and team Id, if both exists then find all the roles having membership that either match with the given userId or the teamId as we need to return the list of roles.

- After making sure the team service and user service has the missing tests for the teamId and userId not found respectively,
- first step was to add a service method having two parameters namely userId and teamId each of the type UUID.
- second step was to add validations if the provided user and team exists.
- third step was to get all the available memberships
- fourth step was to filter the memberships that match with the given userId or teamId
- finally get the roles for the matched memberships

- Added service tests
- Fixed existing RoleApiTest for getRolesByTeamIdAndUserId
- Added new RoleApiTest for getRolesByTeamIdAndUserId
