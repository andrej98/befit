package org.acme;

import io.quarkus.oidc.IdToken;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/workout-plans")
public class WorkoutPlanResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @Counted(name = "workoutplan.getall.counter", description = "How many times was getAll() called.")
    @Timed(name = "workoutplan.getall.timer", unit = MetricUnits.MILLISECONDS)
    @GET
    public List<WorkoutPlan> getAll() {
        return WorkoutPlan.getAllFromUser(idToken.getName());
    }

    @Counted(name = "workoutplan.create.counter", description = "How many plans were created.")
    @Timed(name = "workoutplan.create.timer", unit = MetricUnits.MILLISECONDS)
    @POST
    public Response create(WorkoutPlan plan) {
        plan.userName = idToken.getName();
        List<WorkoutPlan> allFromUser = WorkoutPlan.getAllFromUser(idToken.getName());

        //check if user has any plan with the same name
        if (allFromUser.stream().filter(rec -> rec.name.equals(plan.name)).findFirst().isPresent()){
            throw new BadRequestException(String.format("User %s already has workout plan with name %s", idToken.getName(), plan.name));
        }
        plan.persist();

        WorkoutPlan createdPlan = WorkoutPlan.findByName(plan.name);

        return Response.status(Response.Status.CREATED).entity(createdPlan).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, WorkoutPlan plan) {
        try{
            WorkoutPlan wp = WorkoutPlan.findById(new ObjectId(id));
            if(plan != null && wp.userName.equals(idToken.getName())){
                wp.userName = idToken.getName();
                wp.name = plan.name;
                wp.exercises = plan.exercises;
                wp.frequency = plan.frequency;
                wp.update();
            } else{
                throw new NotFoundException(String.format("You do not have workout plan with id %s", id));
            }
        } catch(IllegalArgumentException e){
            throw new NotFoundException(String.format("You do not have workout plan with id %s", id), e);
        }
        WorkoutPlan updatedPlan = WorkoutPlan.findByName(plan.name);
        return Response.status(Response.Status.OK).entity(updatedPlan).build();
    }

    @Counted(name = "workoutplan.delete.counter", description = "How many plans were deleted.")
    @Timed(name = "workoutplan.delete.timer", unit = MetricUnits.MILLISECONDS)
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        WorkoutPlan wp;
        try {
            wp = WorkoutPlan.findById(new ObjectId(id));
            if (wp != null && wp.userName.equals(idToken.getName())) {
                wp.delete();
            } else {
                throw new NotFoundException(String.format("You do not have workout plan with id %s", id));
            }
        } catch(IllegalArgumentException e) {
            throw new NotFoundException(String.format("You do not have workout plan with id %s", id),e);
        }
        return Response.ok(wp).build();
    }

    @GET
    @Path("/search/{name}")
    public List<WorkoutPlan> search(@PathParam("name") String name) {
        return WorkoutPlan.list("name = ?1 and userName = ?2", name, idToken.getName());
    }

    @GET
    @Path("/{id}")
    public WorkoutPlan get(@PathParam("id") String id) {
        try {
            WorkoutPlan wp = WorkoutPlan.findById(new ObjectId(id));
            if (wp != null && wp.userName.equals(idToken.getName())) {
                return wp;
            } else {
                throw new NotFoundException(String.format("You do not have workout plan with id %s", id));
            }
        } catch(IllegalArgumentException e) {
            throw new NotFoundException(String.format("You do not have workout plan with id %s", id), e);
        }
    }
}