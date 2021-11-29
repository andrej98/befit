package org.acme;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.dto.CreateRecordDTO;
import org.acme.entity.ExerciseRecord;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.oidc.IdToken;
import io.quarkus.panache.common.Parameters;

@Path("/records")
public class RecordResource {

    @Inject
    @IdToken
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExerciseRecord> getRecords(
                    @QueryParam("from") String fromString,
                    @QueryParam("to") String toString,
                    @QueryParam("exercise") String exercise)
    {   
        LocalDate from = null;
        try {
            if (fromString != null) {
                from = LocalDate.parse(fromString);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: from=" + fromString);
        }  
        
        LocalDate to = null;
        try {
            if (toString != null) {
                to = LocalDate.parse(toString);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: to=" + toString);
        }

        StringBuilder query = new StringBuilder().append("authorName = :authorName");
        Parameters params = Parameters.with("authorName", jwt.getName());

        if (from != null) {
            query.append(" and date >= :from");
            params.and("from", from);
        }

        if (to != null) {
            query.append(" and date >= :to");
            params.and("to", to);
        }

        if (exercise != null) {
            query.append(" and exerciseName = :exerciseName");
            params.and("exerciseName", exercise);
        }

        System.out.println(query.toString());
        System.out.println(params.map());
        
        return ExerciseRecord.find(query.toString(), params).list();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ExerciseRecord createRecord(@Valid CreateRecordDTO recordData) {
        ExerciseRecord record = new ExerciseRecord();
        record.authorName = jwt.getName();
        record.exerciseName = recordData.exerciseName;
        record.date = recordData.date.orElse(LocalDate.now());
        record.amount = recordData.amount;
        record.amountType = recordData.amountType;
        record.persist();
        return record;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteRecord(@PathParam long id) {
        ExerciseRecord record = ExerciseRecord.findById(id);
        if (record == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        ExerciseRecord.deleteById(id);
        return Response.noContent().build();
    }
}