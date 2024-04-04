package org.server.controller.services;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SumTimesString implements GetTwoTimesSum {
    @Override
    public String twoTimeSum(Time firstTime, Time secondTime) throws IOException {
        int hoursSelect = firstTime.getHours() + secondTime.getHours();
        int minutesSelect = firstTime.getMinutes() + secondTime.getMinutes();
        int secondsSelect = firstTime.getSeconds() + secondTime.getSeconds();

        if (secondsSelect >= 60) {
            minutesSelect += secondsSelect / 60;
            secondsSelect %= 60;
        }

        if (minutesSelect > 59) {
            hoursSelect += minutesSelect / 60;
            minutesSelect %= 60;
        }

        LocalTime totalTime = LocalTime.of(hoursSelect, minutesSelect, secondsSelect);

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("primeira_volta", firstTime.toString());
        responseData.put("segunda_volta", secondTime.toString());
        responseData.put("total_voltas", totalTime.toString());

        Gson gson = new Gson();
        return gson.toJson(responseData);
    }
}
