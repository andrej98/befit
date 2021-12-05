package org.acme;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.dto.CreateRecordDTO;
import org.acme.dto.ExerciseRecordDTO;
import org.acme.dto.PlanRecordDTO;
import org.acme.entity.ExerciseRecord;
import org.acme.entity.PlanRecord;
import org.acme.mapper.PlanMapper;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.resteasy.annotations.jaxrs.PathParam;


@Path("/records")
@SecurityRequirement(name = "SecurityScheme")
public class RecordResource {

    @Inject
    PlanMapper planMapper;

    @Inject
    JsonWebToken jwt;

    @POST
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Timed(name="record.create.timer")
    public PlanRecordDTO createRecord(@Valid CreateRecordDTO recordData) {
        PlanRecord record = new PlanRecord();
        record.planName = recordData.planName;
        record.authorName = jwt.getName();
        record.date = recordData.date == null ? LocalDate.now() : recordData.date;
        record.persist();
        
        for (ExerciseRecordDTO exerciseDTO : recordData.exercises) {
            ExerciseRecord exercise = new ExerciseRecord();
            exercise.exerciseName = exerciseDTO.exerciseName;
            exercise.amount = exerciseDTO.amount;
            exercise.amountType = exerciseDTO.amountType;
            record.exerciseRecords.add(exercise);
            exercise.planRecord = record;
            exercise.persist();
        }

        return planMapper.toDTO(record);
    }

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name="record.get.timer")
    public List<PlanRecordDTO> getAll() {
        return planMapper.toDTO(PlanRecord.list("authorName", jwt.getName()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    @Transactional
    @Timed(name="record.delete.timer")
    public Response deleteRecord(@PathParam long id) {
        PlanRecord record = PlanRecord.findById(id);
        
        if (record == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        if (!record.authorName.equals(jwt.getName())) {
            // just return NOT_FOUND so we don't leak that
            // this was a valid id
            return Response.status(Status.NOT_FOUND).build();
        }

        for (ExerciseRecord exerciseRecord : record.exerciseRecords) {
            exerciseRecord.delete();
        }
        record.delete();

        return Response.noContent().build();
    }
}