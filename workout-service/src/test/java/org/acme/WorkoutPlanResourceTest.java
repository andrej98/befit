package org.acme;

import io.quarkus.test.mongodb.MongoTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
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
    @TestSecurity(user = "a@gmail.com", roles = "user")
    public void testCreateDeleteWorkoutPlan() {

        Exercise exercise = new Exercise("pushups", "chest", 10, AmountType.REPETITIONS);
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        List<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SUNDAY);
        WorkoutPlan workoutPlan = new WorkoutPlan("Plan A", exercises, days, "a@gmail.com");

        Object id = given()
                .body(workoutPlan)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/workout-plans")
                .then()
                .statusCode(201)
                .body("name", equalTo("Plan A"))
                .body("userName", equalTo("a@gmail.com"))
                .body("exercises.size()", is(1))
                .body("frequency.size()", is(2))
                .extract().path("id");

        given()
                .when().delete("/workout-plans/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Plan A"))
                .body("userName", equalTo("a@gmail.com"))
                .body("exercises.size()", is(1))
                .body("frequency.size()", is(2))
                .body("id", equalTo(id));
    }

    @Test
    @TestSecurity(user = "a@gmail.com", roles = "user")
    public void testCreateUpdateWorkoutPlan() {

        Exercise exercise = new Exercise("pushups", "chest", 10, AmountType.REPETITIONS);
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);
        List<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SUNDAY);
        WorkoutPlan workoutPlan = new WorkoutPlan("Plan C", exercises, days, "a@gmail.com");

        Object id = given()
                .body(workoutPlan)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/workout-plans")
                .then()
                .statusCode(201)
                .body("name", equalTo("Plan C"))
                .body("userName", equalTo("a@gmail.com"))
                .body("exercises.size()", is(1))
                .body("frequency.size()", is(2))
                .extract().path("id");

        WorkoutPlan updatedWorkoutPlan = new WorkoutPlan("Plan B", exercises, days, "a@gmail.com");

        given()
                .body(updatedWorkoutPlan)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().put("/workout-plans/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Plan B"))
                .body("userName", equalTo("a@gmail.com"))
                .body("exercises.size()", is(1))
                .body("frequency.size()", is(2))
                .body("id", equalTo(id));
    }

}