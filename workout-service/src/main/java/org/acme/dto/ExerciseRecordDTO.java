package org.acme.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import com.mongodb.lang.NonNull;

import org.acme.enums.AmountType;

public class ExerciseRecordDTO {
    @NotEmpty
    public String exerciseName;
    @PositiveOrZero
    public Integer amount;
    @NonNull
    public AmountType amountType;
}
