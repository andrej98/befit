package org.acme.clients;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.dto.StatsServiceDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/stats")
@RegisterRestClient(configKey="stats-client")
public interface StatsServiceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_SVG_XML)
    String getStats(StatsServiceDTO statsDto);
}
