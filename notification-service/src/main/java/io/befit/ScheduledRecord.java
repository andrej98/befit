package io.befit;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class ScheduledRecord {

    public String userName;

    public String exerciseName;

    @DecimalMin(value = "0")
    @DecimalMax(value = "23")
    public int time;
}
