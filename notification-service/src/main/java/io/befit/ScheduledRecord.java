package io.befit;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class ScheduledRecord extends PanacheEntity
{
    public String email;

    public String exerciseName;

    public int hour;

    public int minute;

    @Override
    public String toString()
    {
        return "ScheduledRecord{ " + email + ": " + exerciseName +
                ": (" + hour + ":" + minute + ") }";
    }
}
