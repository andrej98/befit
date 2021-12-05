# befit

befit is a revolutionary new fitness tracking application that will help you unlock your potential and take your fitness to the next level. Do you want to have the perfect beach body? Do you want all your friends to envy you? Do you want to acquire the peak of human form? Then this app is for you!

## Description

This app helps users to track their fitness. There are three main functionalities.

1. A user can create workout plans. Each plan consists of one or more exercises and there are four different types of exercises. Depending on the type, the plan also contains an "amount" of the exercise to be done. There can be exercises such as squats which have a certain number of repetitions, exercises like a plank which are performed for a specific time duration, exercises like running which have a distance associated with them and finally one can also specify the number of steps, for example for walking. 

2. It is possible to create a record that a user has performed a workout plan on a given day. These records can then be retrieved and filtered so that the users can see and track their progress. 

3. Users can subscribe to notifications, to be reminded that they are supposed to exercise. The notifications are delivered via email.

## Mikroservices

These three functionalities are split into three microservices.

- `workout-service`: http://localhost:8080/
- `record-service`: http://localhost:8081/
- `notification-service`: http://localhost:8082/

### Additional

- Prometheus: http://localhost:9090/
- Grafana: http://localhost:3000/

### Swagger-UI

- `workout-service`: http://localhost:8080/q/swagger-ui/
- `record-service`: http://localhost:8081/q/swagger-ui/
- `notification-service`: http://localhost:8082/q/swagger-ui/

## Deployment

### Linux

```bash
cd befit
mvn clean package
docker-compose -f docker-compose.yml build
docker-compose -f docker-compose.yml up
```
### Windows

On windows it is not possible to run the main services in docker containers. The only option is to run the supporting services using `docker-compose-dev-win.yml` and then run each service individually either in dev mode or start it from the target folder (but they shouldn't be started in a container). 

Additionally, if one wants to use Grafana, the url for Prometheus should be set to `host.docker.internal:9090`.

## Project description

1. **Description of the project:** ...

1. **Story/scenario of usage:** ...

1. **Why you think a microservice architecture can be appropriate:** ...

1. **Benefits of the using microservices in this project:** ...

1. **Drawbacks of microservices in this case:** ...

## Scenario

1. **Responsiveness:** ...

1. **Resiliency:** ...

1. **Elasticity:** ...

1. **Message-driveness:** ...
