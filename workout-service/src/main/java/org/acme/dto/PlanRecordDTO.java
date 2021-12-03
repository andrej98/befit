package org.acme.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanRecordDTO {
    public Long id;
    public String planName;
    public String authorName;
    public LocalDate date;
    public List<ExerciseRecordDTO> exercises = new ArrayList<>();
}
