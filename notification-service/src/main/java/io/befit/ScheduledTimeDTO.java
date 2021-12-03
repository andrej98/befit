package io.befit;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

public class ScheduledTimeDTO
{
    @DecimalMin(value = "0", message = "Hour cannot be negative")
    @DecimalMax(value = "23", message = "Hour cannot be greater than 23")
    public int hour;

    @DecimalMin(value = "0", message = "Minute cannot be negative")
    @DecimalMax(value = "59", message = "Minute cannot be greater than 23")
    public int minute;

    public ScheduledTimeDTO(int hour, int minute)
    {
        this.hour = hour;
        this.minute = minute;
    }
}
