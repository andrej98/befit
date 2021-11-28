package org.acme.entity;

import javax.persistence.Entity;

import org.acme.enums.AmountType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDate;

@Entity
public class ExerciseRecord extends PanacheEntity {
    public String authorName;
    public String exerciseName;
    public LocalDate date;
    public Integer amount;
    public AmountType amountType;

    @Override
    public String toString() {
        return "Record{" + exerciseName + ": " + amount + "}";
    }
}
