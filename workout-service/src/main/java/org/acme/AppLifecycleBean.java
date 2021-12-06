package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.acme.enums.AmountType;
import org.jboss.logging.Logger;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        List<Exercise> exercises = new ArrayList<>();
        Exercise bench = new Exercise("benchpress", "good for your chest", 10, AmountType.REPETITIONS);
        Exercise pushups = new Exercise("pushups", "bodyweight exercise for your chest", 25, AmountType.REPETITIONS);
        exercises.add(bench);
        exercises.add(pushups);
        List<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SUNDAY);
        WorkoutPlan workoutPlan = new WorkoutPlan("Chest plan",exercises, days, "alice");

        List<Exercise> exercises1 = new ArrayList<>();
        Exercise run = new Exercise("run", "cardio", 2, AmountType.DISTANCE);
        Exercise plank = new Exercise("plank", "great exercise", 30, AmountType.TIME);
        exercises1.add(bench);
        exercises1.add(pushups);
        List<DayOfWeek> days1 = new ArrayList<>();
        days1.add(DayOfWeek.MONDAY);
        days1.add(DayOfWeek.WEDNESDAY);
        WorkoutPlan workoutPlan2 = new WorkoutPlan("Cardio",exercises1, days1, "alice");
        workoutPlan2.persist();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}