package org.telemetry.service;

import org.telemetry.dto.TimeDto;

interface TimeServiceFactory {
    boolean addTimeDto(TimeDto timeDto);
    TimeDto getTimeDto(TimeDto timeDto,int indexTime);
}
