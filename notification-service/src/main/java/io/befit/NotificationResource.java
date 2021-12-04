package io.befit;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import io.quarkus.panache.common.Parameters;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.transaction.Transactional;

@Path("/notifications")
public class NotificationResource
{
    static ScheduledTimeDTO DEFAULT_TIME = new ScheduledTimeDTO(18, 0);

    @Counted(name = "getScheduled.counter",
             description = "How many times notifications were listed")
    @Timed(name = "getScheduled.timer",
           description = "Time it takes to list notifications",
           unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ScheduledRecord> getScheduled(
            @QueryParam("email") String email)
    {
        if (email == null)
        {
            // throw new BadRequestException("Empty: email == null");
            return ScheduledRecord
                .find("", new Parameters())
                .list();
        }

        var query = new StringBuilder()
                .append("email = :email");

        var params = Parameters
                .with("email", email);

        return ScheduledRecord
                .find(query.toString(), params)
                .list();
    }

    @Counted(name = "deleteScheduled.counter",
             description = "How many times notifications were deleted")
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public ScheduledRecord deleteScheduled(@PathParam("id") Long id)
    {
        ScheduledRecord deleted = ScheduledRecord.findById(id);

        if (deleted == null)
        {
            throw new NotFoundException("Nonexistent id: " + id);
        }

        if (!ScheduledRecord.deleteById(id))
            throw new InternalServerErrorException(
                    "Something strange happened "
                    + " while deleting scheduled record: " + id);

        return deleted;
    }

    @Counted(name = "purge.counter",
             description = "How many times notifications were purged")
    @Timed(name = "purge.timer",
           description = "Time it takes to purge all notifications by given email",
           unit = MetricUnits.MILLISECONDS)
    @DELETE
    @Path("/purge/{email}")
    @Transactional
    public Response purge(@PathParam("email") String email)
    {
        if (email == null)
        {
            throw new BadRequestException("Email is empty");
            // return Response.status(Status.BAD_REQUEST).build();
        }

        var query = new StringBuilder()
                .append("email = :email");
        var params = Parameters
                .with("email", email);

        ScheduledRecord.delete(query.toString(), params);
        return Response.noContent().build();
    }


    @Counted(name = "createScheduled.counter",
             description = "How many times notifications were created")
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ScheduledRecord createScheduled(@Valid ScheduledDTO param)
    {
        ScheduledRecord scheduled = new ScheduledRecord();
        scheduled.exerciseName = param.exerciseName;
        scheduled.email = param.email;
        scheduled.hour = param.time.orElse(DEFAULT_TIME).hour;
        scheduled.minute = param.time.orElse(DEFAULT_TIME).minute;
        scheduled.persist();
        return scheduled;
    }
}
