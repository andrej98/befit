package org.acme.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.acme.dto.ExerciseRecordDTO;
import org.acme.dto.PlanRecordDTO;
import org.acme.entity.ExerciseRecord;
import org.acme.entity.PlanRecord;

@ApplicationScoped
public class PlanMapper {
    
    public PlanRecordDTO toDTO(PlanRecord plan) {
        PlanRecordDTO dto = new PlanRecordDTO();
        
        dto.id = plan.id;
        dto.authorName = plan.authorName;
        dto.planName = plan.planName;
        dto.date = plan.date;

        for (ExerciseRecord r : plan.exerciseRecords) {
            ExerciseRecordDTO e = new ExerciseRecordDTO();
            e.exerciseName = r.exerciseName;
            e.amount = r.amount;
            e.amountType = r.amountType;
            dto.exercises.add(e);
        }
        
        return dto;
    }

    public List<PlanRecordDTO> toDTO(List<PlanRecord> plans) {
        List<PlanRecordDTO> dtos = new ArrayList<>();
        
        for (PlanRecord plan : plans) {
            dtos.add(toDTO(plan));
        }
        
        return dtos;
    }
}
