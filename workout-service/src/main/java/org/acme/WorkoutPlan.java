package org.acme;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.DayOfWeek;
import java.util.List;

@MongoEntity(collection="WorkoutPlan")
public class WorkoutPlan extends PanacheMongoEntity {
    public String name;
    public List<Exercise> exercises;
    public List<DayOfWeek> frequency;
    public String userName;

    public WorkoutPlan() {}

    public WorkoutPlan(String name, List<Exercise> exercises, List<DayOfWeek> frequency, String userName) {
        this.name = name;
        this.exercises = exercises;
        this.frequency = frequency;
        this.userName = userName;
    }

    public static WorkoutPlan findByName(String name){
        return find("name", name).firstResult();
    }

    public static List<WorkoutPlan> getAllFromUser(String user){
        return list("userName", user);
    }

    @Override
    public String toString() {
        return "WorkoutPlan{" +
                "name='" + name + '\'' +
                ", exercises=" + exercises +
                ", frequency=" + frequency +
                ", userName='" + userName + '\'' +
                '}';
    }
}
