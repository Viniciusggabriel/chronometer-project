package org.telemetry.controller;

import org.telemetry.dto.TimeDto;
import org.telemetry.service.TimeService;

public class TimeController implements TimeControllerFactory {
    private final TimeService timeService;

    public TimeController() {
        this.timeService = new TimeService();
    }

    @Override
    public boolean addTime(TimeDto timeDto) {
        return timeService.addTimeDto(timeDto);
    }

    @Override
    public TimeDto getTime(TimeDto timeDto, int indexTime) {
        return timeService.getTimeDto(timeDto, indexTime);
    }
}
