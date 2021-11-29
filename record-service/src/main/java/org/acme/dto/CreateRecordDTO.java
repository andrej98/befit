package org.acme.dto;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.acme.enums.AmountType;

public class CreateRecordDTO {
    @NotBlank(message = "Exercise name cannot be empty!")
    public String exerciseName;
    
    @PositiveOrZero(message = "Amount must be non-negative!")
    public Integer amount;
    
    @NotNull(message = "amountType cannot be null!")
    public AmountType amountType;
    
    public Optional<LocalDate> date = Optional.empty();
}
