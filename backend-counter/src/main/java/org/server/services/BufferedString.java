package org.server.services;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Time;

public class BufferedString implements CreateBufferedTime {
    private final Gson gson = new Gson();

    @Override
    public CustomTimer parseJson(String jsonString) {
        return gson.fromJson(jsonString, CustomTimer.class);
    }

    @Override
    public CustomTimer bufferedTimeAndMilliseconds(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        String jsonContent = stringBuilder.toString();

        return parseJson(jsonContent);
    }
}
