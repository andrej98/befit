# befit

befit is a revolutionary new fitness tracking application that will help you unlock your potential and take your fitness to the next level. Do you want to have the perfect beach body? Do you want all your friends to envy you? Do you want to acquire the peak of human form? Then this app is for you!

## Description

This app helps users to track their fitness. There are three main functionalities.

1. A user can create workout plans. Each plan consists of one or more exercises and there are four different types of exercises. Depending on the type, the plan also contains an "amount" of the exercise to be done. There can be exercises such as squats which have a certain number of repetitions, exercises like a plank which are performed for a specific time duration, exercises like running which have a distance associated with them and finally one can also specify the number of steps, for example for walking. 

2. It is possible to create a record that a user has performed a workout plan on a given day. These records can then be retrieved and filtered so that the users can see and track their progress. 

3. Users can subscribe to notifications, to be reminded that they are supposed to exercise. The notifications are delivered via email.

## Microservices

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

#### Linux

```bash
cd befit
mvn clean package
docker-compose -f docker-compose.yml build
docker-compose -f docker-compose.yml up
```


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
