# befit

*PV217 PROJECT*

## Mikroservices

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

1. **Story/scenario of usage:** ...

1. **Why you think a microservice architecture can be appropriate:** ...

1. **Benefits of the using microservices in this project:** ...

1. **Drawbacks of microservices in this case:** ...

## Scenario

1. **Responsiveness:** ...

1. **Resiliency:** ...

1. **Elasticity:** ...

1. **Message-driveness:** ...
