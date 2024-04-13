package org.telemetry.controller.server.routers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.telemetry.controller.TimeController;
import org.telemetry.dto.TimeDto;
import org.telemetry.entity.TimeEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpGetRoute implements HttpRoutesFactory, HttpHandler {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "Essa rota permite apenas o método GET");
        }

        try {
            // Pega os valores do controller
            TimeController timeController = new TimeController();

            TimeDto timeDto = new TimeDto(1, 1, 1, 1);
            TimeDto firstTime = timeController.getTime(timeDto, 1);
            TimeDto secondTime = timeController.getTime(timeDto, 0);

            TimeEntity firstTimeResponse = new TimeEntity(firstTime.milliseconds(), firstTime.seconds(), firstTime.minutes(), firstTime.hours());
            TimeEntity secondTimeResponse = new TimeEntity(secondTime.milliseconds(), secondTime.seconds(), secondTime.minutes(), secondTime.hours());

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("Primeira volta", gson.toJsonTree(firstTimeResponse));
            jsonObject.add("Segunda volta", gson.toJsonTree(secondTimeResponse));

            String jsonResponse = gson.toJson(jsonObject);

            sendResponse(exchange, 200, jsonResponse);
        } catch (IOException error) {
            sendErrorResponse(exchange, "Ouve um erro interno no servidor: " + error);
        }
    }

    @Override
    public void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        // Enviar a resposta no Header da requisição
        exchange.sendResponseHeaders(statusCode, responseMessage.length());
        try (exchange; OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException {
        sendResponse(exchange, 500, errorMessage);
    }
}
