package io.befit;

import java.util.Optional;

import javax.validation.constraints.Email;


public class ScheduledDTO
{
    @Email()
    public String email;

    public String exerciseName;

    public Optional<ScheduledTimeDTO> time = Optional.empty();
}
