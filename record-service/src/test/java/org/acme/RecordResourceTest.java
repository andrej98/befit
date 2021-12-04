package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.acme.dto.CreateRecordDTO;
import org.acme.dto.ExerciseRecordDTO;
import org.acme.dto.PlanRecordDTO;
import org.acme.entity.ExerciseRecord;
import org.acme.entity.PlanRecord;
import org.acme.enums.AmountType;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class RecordResourceTest {

    private CreateRecordDTO createDTO;

    @BeforeEach
    @Transactional
    public void before() {
        // reset db so the test don't influence each other
        ExerciseRecord.deleteAll();
        PlanRecord.deleteAll();
        
        // prepare some useful data so it doesn't have to be done in each
        // test separately
        createDTO = new CreateRecordDTO();
        createDTO.planName = "plan";
        createDTO.date = LocalDate.now();
        createDTO.exercises = new ArrayList<>();

        ExerciseRecordDTO exerciseDto = new ExerciseRecordDTO();
        exerciseDto.amount = 20;
        exerciseDto.exerciseName = "pushups";
        exerciseDto.amountType = AmountType.REPETITIONS;

        createDTO.exercises.add(exerciseDto);
    }


    @Test
    @TestSecurity(user = "alice", roles = {"user"})
    public void testNormalUsage() {        
        PlanRecordDTO res1 =
            given()
                .contentType(ContentType.JSON)
                .body(createDTO, ObjectMapperType.JSONB)
            .when()
                .post("/records")
            .then()
                .statusCode(200)
                .body("planName", equalTo(createDTO.planName))
                .body("authorName", equalTo("alice"))
                .body("date", equalTo(createDTO.date.toString()))
            .extract()
                .as(PlanRecordDTO.class, ObjectMapperType.JSONB);

        createDTO.planName = "something-else";
        createDTO.date = LocalDate.now().minusDays(2);
        PlanRecordDTO res2 =
            given()
                .contentType(ContentType.JSON)
                .body(createDTO, ObjectMapperType.JSONB)
            .when()
                .post("/records")
            .then()
                .statusCode(200)
                .body("planName", equalTo(createDTO.planName))
                .body("authorName", equalTo("alice"))
                .body("date", equalTo(createDTO.date.toString()))
            .extract()
                .as(PlanRecordDTO.class, ObjectMapperType.JSONB);

        given()
            .when().get("/records")
            .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("id", containsInAnyOrder(res1.id.intValue(), res2.id.intValue()))
                .body("planName", containsInAnyOrder("plan", "something-else"))
                .body("authorName", everyItem(equalTo("alice")))
                .body("date", containsInAnyOrder(LocalDate.now().toString(), LocalDate.now().minusDays(2).toString()));

        given().pathParam("id", res1.id)
            .when().delete("/records/{id}")
            .then().statusCode(204);

        given()
            .when().get("/records")
            .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("id", containsInAnyOrder(res2.id.intValue()))
                .body("planName", containsInAnyOrder("something-else"))
                .body("authorName", everyItem(equalTo("alice")))
                .body("date", containsInAnyOrder(createDTO.date.toString()));
    }

    @Test
    @TestSecurity(user = "alice", roles = {"user"})
    public void testValidation() {
        createDTO.planName = "";
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        createDTO.planName = "plan";

        createDTO.date = LocalDate.now().plusDays(5);
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        createDTO.date = LocalDate.now();

        List<ExerciseRecordDTO> exercises = createDTO.exercises;
        ExerciseRecordDTO e = createDTO.exercises.get(0);

        createDTO.exercises = null;
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        createDTO.exercises = exercises;

        createDTO.exercises.clear();
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        createDTO.exercises.add(e);

        e.exerciseName = "";
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        e.exerciseName = "squats";

        e.amount = -1;
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        e.amount = 10;

        e.amountType = null;
        given().contentType(ContentType.JSON).body(createDTO, ObjectMapperType.JSONB)
            .when().post("/records").then().statusCode(400);
        e.amountType = AmountType.REPETITIONS;

        given().when().get("/records").then().body(is("[]"));
    }

    @Test
    public void testAnauthorizedAccess() {
        given()
            .when()
                .get("/records")
            .then()
                .statusCode(401);

        given()
                .contentType(ContentType.JSON)
                .body(createDTO, ObjectMapperType.JSONB)
            .when()
                .post("/records")
            .then()
                .statusCode(401);
        
        given()
            .when()
                .delete("/records/5")
            .then()
                .statusCode(401);
    }

}