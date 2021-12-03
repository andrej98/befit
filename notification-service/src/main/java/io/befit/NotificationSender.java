package io.befit;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class NotificationSender
{
    @Inject
    EmailResource emailResource;

    @ConfigProperty(name = "befit.timezone", defaultValue = "0")
    String timezone;

    @ConfigProperty(name = "quarkus.mailer.mock")
    String emailMock;

    int prevHour = -1,
        prevMin = -1;

    // make sure it's a prime number of seconds
    @Scheduled(every="23s")
    void checkTime()
    {
        if (emailMock == null || emailMock.equals("true"))
        {
            Log.warn("Email mock is enabled, no emails will be sent! "
                    + "To disable, set BEFIT_MOCK_EMAIL=false");
        }

        TimeZone tz = TimeZone.getTimeZone(timezone);

        int currentHour = Calendar.getInstance(tz).get(Calendar.HOUR_OF_DAY),
            currentMin = Calendar.getInstance(tz).get(Calendar.MINUTE);

        if (prevHour == currentHour && prevMin == currentMin)
            return;

        prevHour = currentHour;
        prevMin = currentMin;

        sendEmails(currentHour, currentMin);
    }

    void sendEmails(int hour, int minute)
    {
        var query = new StringBuilder()
        .append("hour = :hour");

        var params = Parameters
                .with("hour", hour);

        List<ScheduledRecord> selected = ScheduledRecord
            .find(query.toString(), params)
            .list();

        for (var record : selected)
        {
            if (record.hour == hour && record.minute == minute)
            {
                Log.info("[" + hour + ":" + minute + "] Sending email to: "
                            + record.email);
                emailResource.sendEmail(record.email, record.exerciseName);
            }
        }
    }
}
