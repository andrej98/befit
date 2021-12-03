package org.acme.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

public class NewRecordDTO {
    @NotBlank
    public String planName;
    @PastOrPresent
    public LocalDate date;
}
