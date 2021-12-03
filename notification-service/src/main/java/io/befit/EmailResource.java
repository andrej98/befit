package io.befit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

@Path("/notifications/email")
public class EmailResource
{
    @Inject
    Mailer mailer;

    @Blocking
    public void sendEmail(String address, String exerciseName) {
        mailer.send(Mail
            .withText(address,
                    "Befit exercise notification",
                    "Hi, don't forget your exercise plan: '" + exerciseName + "'"));
    }
}
