package org.telemetry.controller.server.routers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.telemetry.controller.TimeController;
import org.telemetry.dto.TimeDto;
import org.telemetry.entity.TimeEntity;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpPostRoute implements HttpRoutesFactory, HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "Essa rota permite apenas o método POST");
            return;
        }

        try {
            try (InputStream inputStream = exchange.getRequestBody()) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        // Todo: buffer para ler corpo da requisição
                        StringBuilder requestBody = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            requestBody.append(line);
                        }

                        TimeEntity jsonBuilder = gson.fromJson(requestBody.toString(), TimeEntity.class);

                        // Adiciona ao controller os dados recebidos
                        TimeController timeController = new TimeController();

                        TimeDto timeDto = new TimeDto(jsonBuilder.getMilliseconds(), jsonBuilder.getSeconds(), jsonBuilder.getMinutes(), jsonBuilder.getHours());
                        timeController.addTime(timeDto);

                        sendResponse(exchange, 201, "Criado com sucesso");
                    }
                } catch (IOException error) {
                    sendErrorResponse(exchange, "Erro ao preparar o buffer: " + error);
                }
            } catch (IOException error) {
                sendErrorResponse(exchange, "Erro ao ler sua requisição: " + error);
            }
        } catch (IOException error) {
            sendErrorResponse(exchange, "Erro ao preparar leitura da sua requisição: " + error);
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
