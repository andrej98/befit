package org.acme.clients;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.dto.PlanRecordDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.oidc.token.propagation.AccessToken;

@Path("/records")
@AccessToken
@RegisterRestClient(configKey="records-client")
public interface RecordServiceClient {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PlanRecordDTO createRecord(PlanRecordDTO recordData);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<PlanRecordDTO> getAll();

    @DELETE
    @Path("/{id}")
    Response deleteRecord(@PathParam long id);

}
