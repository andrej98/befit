package org.acme;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.clients.RecordServiceClient;
import org.acme.clients.StatsServiceClient;
import org.acme.dto.NewRecordDTO;
import org.acme.dto.ExerciseRecordDTO;
import org.acme.dto.PlanRecordDTO;
import org.acme.dto.StatsServiceDTO;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.oidc.IdToken;

@Path("/records")
public class RecordResource {
    
    @Inject
    @IdToken
    JsonWebToken jwt;

    @Inject
    @RestClient
    RecordServiceClient recordServiceClient;

    @Inject
    @RestClient
    StatsServiceClient statsServiceClient;

    @POST
    @Retry(maxRetries = 4)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PlanRecordDTO createRecord(NewRecordDTO recordDto) {
        PlanRecordDTO planDTO = new PlanRecordDTO();
        planDTO.authorName = jwt.getName();
        planDTO.planName = recordDto.planName;
        planDTO.date = recordDto.date == null ? LocalDate.now() : recordDto.date;
        planDTO.exercises = new ArrayList<>();

        PanacheQuery<WorkoutPlan> plans = WorkoutPlan
                        .find("name = ?1 and userName = ?2", planDTO.planName, planDTO.authorName);

        if (plans.count() == 0) {
            throw new NotFoundException(Response.status(Status.NOT_FOUND)
                                            .entity("Plan '" + planDTO.planName + "' does not exist.")
                                            .type(MediaType.TEXT_PLAIN)
                                            .build());
        }
        
        for (Exercise exercise : plans.singleResult().exercises) {
            ExerciseRecordDTO e = new ExerciseRecordDTO();
            e.exerciseName = exercise.name;
            e.amount = exercise.amount;
            e.amountType = exercise.amountType;
            planDTO.exercises.add(e);
        }

        return recordServiceClient.createRecord(planDTO);
    }


    @GET
    @Path("/stats")
    // @Retry(maxRetries = 4)
    @Produces(MediaType.APPLICATION_SVG_XML)
    public String getStats(@QueryParam("from") String fromString,
                            @QueryParam("to") String toString,
                            @QueryParam("plan") String planName)
    {
        var param = new StatsServiceDTO();
        return statsServiceClient.getStats(param);
    }


    @GET
    @Retry(maxRetries = 4)
    @Fallback(fallbackMethod = "recordServiceFallback")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanRecordDTO> getRecords(
                            @QueryParam("from") String fromString,
                            @QueryParam("to") String toString,
                            @QueryParam("plan") String planName)
    {
        LocalDate from = parseDate(fromString); 
        LocalDate to = parseDate(toString);

        Stream<PlanRecordDTO> records = recordServiceClient.getAll().stream();

        if (planName != null) {
            records = records.filter(r -> r.planName.equals(planName));
        }
        if (from != null) {
            records = records.filter(r -> r.date.isAfter(from) || r.date.isEqual(from));
        }
        if (to != null) {
            records = records.filter(r -> r.date.isBefore(to) || r.date.isEqual(to));
        }

        return records.collect(Collectors.toList());
    }


    @DELETE
    @Retry(maxRetries = 4)
    @Path("/{recordId}")
    public Response deleteRecord(@QueryParam Long recordId) {
        try {
            recordServiceClient.deleteRecord(recordId);
        } catch(WebApplicationException e) {
            // "log" the exception
            System.err.println(e);
            
            Response r = e.getResponse();
            
            // create a new response so we don't leak to much internal errors
            return Response.status(r.getStatus()).build();
        }

        return Response.noContent().build();
    }


    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new BadRequestException(Response.status(Status.BAD_REQUEST)
                                            .entity("Invalid date format '" + date + "'.")
                                            .type(MediaType.TEXT_PLAIN)
                                            .build());
        }
    }

    private List<PlanRecordDTO> recordServiceFallback(
            @QueryParam("from") String fromString,
            @QueryParam("to") String toString,
            @QueryParam("plan") String planName) {

        throw new ServiceUnavailableException(Response.status(Status.SERVICE_UNAVAILABLE)
                .entity("Record service is not available.")
                .type(MediaType.TEXT_PLAIN)
                .build());
    }
}
