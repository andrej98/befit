package org.acme.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;

public class CreateRecordDTO {
    @NotBlank
    public String planName;

    @PastOrPresent
    public LocalDate date;

    @NotEmpty
    public List<ExerciseRecordDTO> exercises;
}
