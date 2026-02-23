package com.draculavenom.schedulingSystem.dto;

import java.time.LocalTime;

import com.draculavenom.schedulingSystem.model.DayOfWeekEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayShiftRequest {
    private DayOfWeekEnum day;
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Integer appointmentDuration;    
}
