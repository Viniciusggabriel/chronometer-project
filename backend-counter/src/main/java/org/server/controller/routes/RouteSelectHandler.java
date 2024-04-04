package org.server.controller.routes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.controller.services.SumTimesString;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dao.dto.SelectResponseOperation;
import org.server.dao.dto.OperationResult;
import org.server.dao.query.QueryExecutorSelect;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Time;

public class RouteSelectHandler implements HttpHandler, ErrorHttpFactory {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        /*
         * Verifica se o método é GET
         * Inicia uma variável do tipo SelectResponseOperation que é um record que recebe e retorna outros dois records
         * Verifica se o resultado é = 0 e retorna erro ou o resultado
         * Usa um service que faz a soma dos dois valores obtidos, se for erro entra no catch
         * verifica se a resposta é um erro do banco ou HTTP e retorna esse erro
         * */

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "Essa rota permite apenas o método GET");
        }

        SelectResponseOperation resultSelectClock = null;
        try {
            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorSelect dataBase = new QueryExecutorSelect(connectionManager);

            resultSelectClock = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP");

            if (resultSelectClock.selectResponse().resultSelect().length == 0) {
                sendErrorResponse(exchange, "Requisição tem tamanho zero");
            } else {
                Time firstLap = (Time) resultSelectClock.selectResponse().resultSelect()[1];
                Time secondLap = (Time) resultSelectClock.selectResponse().resultSelect()[0];

                SumTimesString sumTimesString = new SumTimesString();

                try {
                    sendResponse(exchange, 200, sumTimesString.twoTimeSum(firstLap, secondLap));
                } catch (IOException error) {
                    sendResponse(exchange, 102, "Processando");
                }
            }
        } catch (IOException error) {
            if (resultSelectClock != null) {
                OperationResult operationResult = resultSelectClock.operationResult();

                // verifica se a resposta de erro é do banco de dados e retorna ela ou retorna o erro do HTTP
                if (operationResult.operationCode() == 500) {
                    sendErrorResponse(exchange, operationResult.operationMessage());
                } else {
                    sendErrorResponse(exchange, operationResult.operationMessage() + error.getMessage());
                }
            } else {
                // Caso o resultSelectClock
                sendErrorResponse(exchange, "Erro interno no servidor");
            }
        }
    }

    @Override
    public void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException {
        // Configurar o tipo de conteúdo como application/json
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        String jsonResponse = responseMessage;

        if (statusCode != 200) {
            OperationResult operationResult = new OperationResult(statusCode, responseMessage);
            jsonResponse = gson.toJson(operationResult);
        }

        // Enviar a resposta no Header da requisição
        exchange.sendResponseHeaders(statusCode, jsonResponse.length());
        try (exchange; OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }

    }

    @Override
    public void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException {
        sendResponse(exchange, 500, errorMessage);
    }
}