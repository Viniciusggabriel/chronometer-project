package org.server.controller.routes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.services.BufferedString;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dto.OperationResult;
import org.server.dao.query.QueryExecutorInsert;
import org.server.services.CustomTimer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RouteInsertHandler implements HttpHandler, ErrorHttpFactory {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        /*
         * Verifica se o método é POST
         * Tenta ler o corpo da requisição e criar um buffer, instancia uma classe passando o buffer o retorno da classe é passado como parâmetro a query insert
         * Usa o método sendResponse para mandar o erro da classe QueryExecutorInsert ou a mensagem de sucesso
         * Trata um erro com a solicitação HTTP
         * */

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "Essa rota permite apenas o método POST");
            return;
        }

        try (InputStream requestBody = exchange.getRequestBody();
             InputStreamReader inputStreamReader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            CustomTimer result;

            try {
                BufferedString bufferedString = new BufferedString();
                result = bufferedString.bufferedTimeAndMilliseconds(bufferedReader);
            } catch (IOException error) {
                sendResponse(exchange, 102, "Erro ao ler corpo da requisição: " + error.getMessage());
                return;
            }

            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorInsert queryExecutorInsert = new QueryExecutorInsert(connectionManager);

            // Pega os valores do json
            String resultTime = result.time();
            long resultMilliseconds = result.milliseconds();

            OperationResult response = queryExecutorInsert.executeInsert("CALL INSERT_DATA_TIME('" + resultTime + "'," + resultMilliseconds + ");");

            // Enviar o resultado do response, pode ser um erro ou o resultado em si
            sendResponse(exchange, response.operationCode(), response.operationMessage());
        } catch (IOException error) {
            // Envia uma resposta de erro caso o servidor HTTP não consiga enviar dados
            sendErrorResponse(exchange, "Ocorreu um erro ao processar a sua solicitação HTTP: " + error.getMessage());
        }
    }

    @Override
    public void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException {
        // Configurar o tipo de conteúdo como application/json
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        OperationResult operationResult = new OperationResult(statusCode, responseMessage);

        String jsonResponse = gson.toJson(operationResult);

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
