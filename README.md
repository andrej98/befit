# befit

befit is a revolutionary new fitness tracking application that will help you unlock your potential and take your fitness to the next level. Do you want to have the perfect beach body? Do you want all your friends to envy you? Do you want to acquire the peak of human form? Then this app is for you!

## Description

This app helps users to track their fitness. There are three main functionalities.

1. A user can create workout plans. Each plan consists of one or more exercises and there are four different types of exercises. Depending on the type, the plan also contains an "amount" of the exercise to be done. There can be exercises such as squats which have a certain number of repetitions, exercises like a plank which are performed for a specific time duration, exercises like running which have a distance associated with them and finally one can also specify the number of steps, for example for walking. 

2. It is possible to create a record that a user has performed a workout plan on a given day. These records can then be retrieved and filtered so that the users can see and track their progress. 

3. Users can subscribe to notifications, to be reminded that they are supposed to exercise. The notifications are delivered via email.

## Microservices

There are three microservices: the main one is `workout-service` and the remaining two are `notification-service` and `record-service`. Only `workout-service` and `notification-service` are "front-facing", meaning they can be used directly through the swagger-ui. `record-service` has swagger-ui as well, but it is harder to use. As it is a "backend" service it doesn't redirect the user to a login screen and one must first obtain an access token through another service. This is practically possible only in dev mode. More on this [here](#record-service).

### workout-service
 - url: http://localhost:8080/
 - Swagger-ui: http://localhost:8080/q/swagger-ui/

### notification service
 - url: http://localhost:8082/
 - Swagger-ui: http://localhost:8082/q/swagger-ui/

### record-service
 - url: http://localhost:8082/
 - Swagger-ui: http://localhost:8082/q/swagger-ui/

To use the swagger-ui, one most provide it with an access token. To do that start the `workout-service` service in dev mode and perform these steps:

TODO

### Additional

- Prometheus: http://localhost:9090/
- Grafana: http://localhost:3000/
- Jaeger: http://localhost:16686/
- Keycloak administration: http://localhost:8180/ 

### Keycloak

For managing users we use _keycloak_. There are three user accounts that can be used. It is also possible to register as a new user on the login screen.

| **Username**   | **Password** |  **Roles**    |
|:--             |:--           |:--            |
| alice          | alice        | user          |
| jdoe           | jdoe         | user          |
| admin          | admin        | user, admin   |

To logout use one of the following URLs:
 - http://localhost:8080/logout
 - http://localhost:8082/logout


## Deployment

### Linux

```bash
cd befit
mvn clean package
docker-compose -f docker-compose.yml build
docker-compose -f docker-compose.yml up
```

Or to use the services in dev mode, first start the supporting services

```bash
docker-compose -f docker-compose-dev.yml up
```

And then enter the root directory of each service and run

```
./mvnw clean compile quarkus:dev
```

### Windows

On windows it is not possible to run the main services in docker containers. The only option is to run the supporting services using `docker-compose-dev-win.yml` and then run each service individually either in dev mode or start it from the target folder (but they shouldn't be started in a container). 

Additionally, if one wants to use Grafana, the url for Prometheus should be set to `host.docker.internal:9090`.

## Project description

1. **Description of the project:** ...

2. **Story/scenario of usage:**

    - User creates a workout plan _“Workout Routine 1”_ consisting of
    exercises _“Running, Planking, Pushups”_ and daily frequency

    - On Monday, User records that they have _run_ for `3km`, _planked_
    for `74s` and done `30` _pushups_

    - On Tuesday, User records that they have _run_ for `0km`, _planked_
    for `20s` and done `10` _pushups_

    - On Wednesday, User forgets to workout and doesn't record any exercise

    - Next day, User sets up optional daily notifications at _17:05_ in order
    not to forget again

    - On Friday, User receives an e-mail from `befit.notify@gmail.com` that
    they should do their _“Workout Routine 1”_

3. **Why you think a microservice architecture can be appropriate:**

    - Because there doesn't need to be much interaction between users,
    that makes the whole system easier to split into multiple closed units.
    Each feature of the system can be represented as a standalone microservice
    calling other microservices when needed. User then accesses the
    microservice that gives them the functionality they currently need.

4. **Benefits of the using microservices in this project:**

    - Ease of development: each student can take care of their own microservice
    and not be bothered with others intervening on their work.

5. **Drawbacks of microservices in this case:**

    - For some parts of the system it would be more intuitive to make them
    as a monolith – namely `workout service` and `record service`, because
    these two have to communicate frequently to provide the desired
    functionality.

## Scenario

1. **Responsiveness:** ...

1. **Resiliency:** ...

1. **Elasticity:** ...

1. **Message-driveness:** ...
