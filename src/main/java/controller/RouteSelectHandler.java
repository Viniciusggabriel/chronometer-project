package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.DatabaseConnectionManage;
import model.QueryExecutorSele;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class RouteSelectHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod(); // Exchange usado pelo cliente para realizar as solicitações

        // Se o metodo for diferente de GET manda erro
        if (!method.equals("GET")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
        QueryExecutorSele dataBase = new QueryExecutorSele(connectionManager);

        Object[] resultSelectClockFullValues;
        try {
            resultSelectClockFullValues = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP");
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }

        if (resultSelectClockFullValues.length == 0) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }

        Time firstValue = (Time) resultSelectClockFullValues[1];
        Time penultimateValue = (Time) resultSelectClockFullValues[0];

        int hoursSelect = firstValue.getHours() + penultimateValue.getHours();
        int minutesSelect = firstValue.getMinutes() + penultimateValue.getMinutes();
        int secondsSelect = firstValue.getSeconds() + penultimateValue.getSeconds();

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
        responseData.put("primeira_volta", firstValue.toString());
        responseData.put("segunda_volta", penultimateValue.toString());
        responseData.put("total_voltas", totalTime.toString());

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.length());

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }
}
