package org.acme.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;

public class NewExerciseRecordDTO {
    @PastOrPresent
    public LocalDate date;
    @NotEmpty
    public List<ExerciseRecordDTO> exercises = new ArrayList<>();
}
