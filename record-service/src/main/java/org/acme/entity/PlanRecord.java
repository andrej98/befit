package org.acme.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class PlanRecord extends PanacheEntity {
    public String planName;
    public String authorName;
    public LocalDate date;
    @OneToMany(mappedBy = "planRecord")
    public Set<ExerciseRecord> exerciseRecords = new HashSet<>();

    @Override
    public String toString() {
        return "PlanRecord{" +
                "planName=" + planName + ", " + 
                "authorName=" + authorName + ", " + 
                "date=" + date + "}";
    }
}
