package org.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Time;

interface CreateBufferedTime {
    CustomTimer parseJson(String jsonString);
    CustomTimer bufferedTimeAndMilliseconds(BufferedReader bufferedReader) throws IOException;
}

interface GetTwoTimesSum {
    String twoTimeSum(Time firstTime, Time secondTime) throws IOException;
}