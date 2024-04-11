package org.server.dto;

import java.sql.Time;
import java.util.List;

public record OperationResponse(List<Time> resultTime, List<Long> resultMilliseconds) {
}
