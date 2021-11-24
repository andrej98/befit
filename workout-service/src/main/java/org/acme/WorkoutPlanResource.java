package org.acme;

import io.quarkus.oidc.IdToken;
import io.quarkus.panache.common.Parameters;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/workout-plans")
public class WorkoutPlanResource {

    @Inject
    @IdToken
    JsonWebToken idToken;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkoutPlan> getAll() {
        return WorkoutPlan.getAllFromUser(idToken.getName());
    }

    @POST
    public Response create(WorkoutPlan plan) {
        plan.userName = idToken.getName();
        plan.persist();
        return Response.created(URI.create("/workout-plans/" + plan.id)).build();
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, WorkoutPlan plan) throws Exception {
        try{
            WorkoutPlan wp = WorkoutPlan.findById(new ObjectId(id));

            if(plan != null && wp.userName.equals(idToken.getName())){
                plan.userName = idToken.getName();
                plan.update();
            } else{
                throw new NotFoundException(String.format("You do not have workout plan with id %s", id));
            }
        } catch(IllegalArgumentException e){
            throw new NotFoundException(String.format("You do not have workout plan with id %s", id), e);
        }

    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        try {
            WorkoutPlan wp = WorkoutPlan.findById(new ObjectId(id));
            if (wp != null && wp.userName.equals(idToken.getName())) {
                wp.delete();
            } else {
                throw new NotFoundException(String.format("You do not have workout plan with id %s", id));
            }
        } catch(IllegalArgumentException e) {
            throw new NotFoundException(String.format("You do not have workout plan with id %s", id),e);
        }
    }

    @GET
    @Path("/search/{name}")
    public List<WorkoutPlan> search(@PathParam("name") String name) {
        return WorkoutPlan.list("name like :name", Parameters.with("name", "%" + name + "%"));

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

    @DELETE
    @Path("/all")
    public void dropAll(){
        List<WorkoutPlan> plans = WorkoutPlan.listAll();
        plans.forEach(plan -> plan.delete());
    }
}