package io.befit;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

@Path("/notifications/email")
public class EmailResource
{
    @Inject
    Mailer mailer;

    @Counted(name = "sendEmail.counter",
             description = "How many emails were sent")
    @Timed(name = "sendEmail.timer",
           description = "Time it takes to send an email",
           unit = MetricUnits.MILLISECONDS)
    @Blocking
    public void sendEmail(String address, String exerciseName)
    {
        mailer.send(Mail
            .withText(address,
                    "Befit exercise notification",
                    "Hi, don't forget your exercise plan: '" + exerciseName + "'"));
    }
}
