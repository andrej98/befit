package org.acme;

import io.quarkus.oidc.token.propagation.JsonWebToken;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.mongodb.MongoTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.ConfigMetadata;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.restassured.RestAssured;
import org.acme.enums.AmountType;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(MongoTestResource.class)
public class WorkoutPlanResourceTest {

    @Test
    @TestSecurity(user = "alice", roles = "user")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "user@gmail.com")
    }, userinfo = {
            @UserInfo(key = "sub", value = "subject")
    }, config = {
            @ConfigMetadata(key = "issuer", value = "issuer")
    })
    public void testCreateWorkoutPlan() {

        RestAssured.when().get("workout-plans").then()
                .statusCode(200);

//        Exercise exercise = new Exercise("pushups", "chest", 10, AmountType.REPETITIONS);
//        List<Exercise> exercises = new ArrayList<>();
//        exercises.add(exercise);
//        List<DayOfWeek> days = new ArrayList<>();
//        days.add(DayOfWeek.FRIDAY);
//        days.add(DayOfWeek.SUNDAY);
//        WorkoutPlan workoutPlan = new WorkoutPlan("Plan A", exercises, days, "a@gmail.com");
//
//        Object id = given()
//                .body(workoutPlan)
//                .contentType("application/json")
//                .when().post("/workout-plans")
//                .then()
//                .statusCode(201)
//                .body("name", equalTo("Plan A"))
//                .body("userName", equalTo("a@gmail.com"))
//                .body("exercises.size()", is(1))
//                .body("frequency.size()", is(2))
//                .extract().path("id");

    }




}