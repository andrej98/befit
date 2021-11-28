package org.acme;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.dto.CreateRecordDTO;
import org.acme.entity.ExerciseRecord;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.oidc.IdToken;

@Path("/records")
public class RecordResource {

    @Inject
    @IdToken
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExerciseRecord> getAllRecords() {
        System.out.println("NAME: " + jwt.getName());
        return ExerciseRecord.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ExerciseRecord createRecord(CreateRecordDTO recordData) {
        ExerciseRecord record = new ExerciseRecord();
        record.authorName = recordData.authorName;
        record.exerciseName = recordData.exerciseName;
        record.date = recordData.date;
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