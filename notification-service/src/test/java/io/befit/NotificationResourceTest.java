package io.befit;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class NotificationResourceTest
{
    @Test
    public void testDefaultDB()
    {
        given()
            .when()
                .get("/notifications/list")
            .then()
            .statusCode(200)
            .body("size()", is(4))
            .body("email", containsInAnyOrder("franta@nonexistent.com",
                                             "nonexistent.email@nonexistent.com",
                                             "someone@nonexistent.com",
                                             "franta@nonexistent.com"))
            .body("exerciseName", containsInAnyOrder("Pushups",
                                                     "Pushups",
                                                     "Running",
                                                     "Walking"))
            .body("hour", containsInAnyOrder(18, 19, 20, 21))
            .body("minute", containsInAnyOrder(1, 2, 3, 4));
    }

    @Test
    public void testEmailSearch()
    {
        given()
            .when()
                .param("email", "nonexistent.email@nonexistent.com")
                .get("/notifications/list")
            .then()
            .statusCode(200)
            .body("email", containsInAnyOrder("nonexistent.email@nonexistent.com"))
            .body("exerciseName", containsInAnyOrder("Pushups"))
            .body("hour", containsInAnyOrder(19))
            .body("minute", containsInAnyOrder(2));

        given()
            .when()
                .param("email", "franta@nonexistent.com")
                .get("/notifications/list")
            .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("email", containsInAnyOrder("franta@nonexistent.com",
                                              "franta@nonexistent.com"));
    }

    @Test
    public void testAddPurge()
    {
        String[] emails = new String[]
        {
            "test1@nonexistentbb.com", "test2@nonexistentcc.com",
            "test3@nonexistentaa.com"
        };

        for (String email : emails)
        {
            var param = new ScheduledDTO();
            param.time = Optional.empty();
            param.email = email;
            param.exerciseName = "trolololol";

            var id = given()
                .body(param)
                    .contentType("application/json")
                    .post("/notifications/create")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("email", equalTo(param.email))
                .body("exerciseName", equalTo(param.exerciseName))
                .extract()
                .path("id");

            given()
                .when()
                    .delete("/notifications/delete/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("email", equalTo(param.email))
                .body("exerciseName", equalTo(param.exerciseName))
                .body("id", equalTo(id));

            given()
                .when()
                    .get("/notifications/list")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("email", not(hasItem(email)));
        }

        given()
            .when()
                .delete("/notifications/purge")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}

