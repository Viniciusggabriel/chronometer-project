package org.server.controller.routes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dao.dto.ResultSelect;
import org.server.dao.query.QueryExecutorSelect;

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
        // Se o metodo for diferente de GET manda erro
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            Object[] resultSelectClockFullValues;

            try {
                DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
                QueryExecutorSelect dataBase = new QueryExecutorSelect(connectionManager);

                resultSelectClockFullValues = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP");

                if (resultSelectClockFullValues.length == 0) {
                    exchange.sendResponseHeaders(404, 0);

                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write("Sem dados dentro do banco".getBytes());

                    outputStream.close();
                    exchange.close();
                } else {
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
            } catch (IOException error) {
                exchange.sendResponseHeaders(404, 0);

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write("Erro ao buscar dados dentro do banco, tente novamente ou entre em contato com o suporte".getBytes());

                outputStream.close();
                exchange.close();
            }

        } else {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write("Essa rota permite apenas o método Get".getBytes());

            outputStream.close();
            exchange.close();
        }
    }
}