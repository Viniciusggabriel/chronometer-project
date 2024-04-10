package org.server.services;

import java.sql.Time;

public record CustomTimer(Time time, long milliseconds) {
}
