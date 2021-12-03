package org.acme.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.acme.enums.AmountType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class ExerciseRecord extends PanacheEntity {
    public String exerciseName;
    public Integer amount;
    public AmountType amountType;
    @ManyToOne
    @JoinColumn(nullable = false)
    public PlanRecord planRecord;

    @Override
    public String toString() {
        return "ExerviseRecord{" +
                "exercise=" + exerciseName + ", " + 
                "amout=" + amount + ", " + 
                "amountType=" + amountType + "}";
    }
}
