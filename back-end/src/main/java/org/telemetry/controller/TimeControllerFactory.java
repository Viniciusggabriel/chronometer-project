package org.telemetry.controller;

import org.telemetry.dto.TimeDto;

interface TimeControllerFactory {
    boolean addTime(TimeDto timeDto);

    TimeDto getTime(TimeDto timeDto,int indexTime);
}
