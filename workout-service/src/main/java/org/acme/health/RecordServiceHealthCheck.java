package org.acme.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@Readiness
@ApplicationScoped
public class RecordServiceHealthCheck implements HealthCheck {
    @ConfigProperty(name = "quarkus.rest-client.records-client.url")
    String url;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.builder()
                .name("Record service health check");

        Client client = null;

        try {
            client = ClientBuilder.newClient();
            Response response = client.target(url).path("/q/health")
                    .request().get();
            response.close();
            builder.up();
        } catch (Exception e) {
            builder.down()
                    .withData("exception message", e.getMessage());
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return builder.build();
    }
}
