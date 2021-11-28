package org.acme.dto;

import java.time.LocalDate;

import org.acme.enums.AmountType;

public class CreateRecordDTO {
    public String authorName;
    public String exerciseName;
    public LocalDate date;
    public Integer amount;
    public AmountType amountType;
}
