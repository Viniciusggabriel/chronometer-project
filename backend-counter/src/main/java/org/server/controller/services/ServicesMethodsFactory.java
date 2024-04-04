package org.server.controller.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Time;

interface CreateBufferedTime {
    Time bufferedTime(BufferedReader bufferedReader) throws IOException;
}

interface GetTwoTimesSum {
    String twoTimeSum(Time firstTime, Time secondTime) throws IOException;
}