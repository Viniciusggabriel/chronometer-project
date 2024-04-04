package org.server.controller.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.controller.services.BufferedString;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dao.dto.OperationResult;
import org.server.dao.query.QueryExecutorInsert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Time;

public class RouteInsertHandler implements HttpHandler, ErrorHttpFactory {
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

        try (InputStream requestBody = exchange.getRequestBody(); InputStreamReader inputStreamReader = new InputStreamReader(requestBody, StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            Time result;

            try {
                BufferedString bufferedString = new BufferedString();
                result = bufferedString.bufferedTime(bufferedReader);

            } catch (IOException error) {
                sendResponse(exchange, 102, "Erro ao ler corpo da requisição" + error.getMessage());
                return;
            }

            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorInsert queryExecutorInsert = new QueryExecutorInsert(connectionManager);

            OperationResult response = queryExecutorInsert.executeInsert("CALL INSERT_DATA_TIME('" + result + "');");

            // Enviar o resultado do response, pode ser um erro ou o resultado em si
            sendResponse(exchange, response.errorCode(), response.errorMessage());
        } catch (IOException error) {
            // Envia uma resposta de erro caso o servidor HTTP não consiga enviar dados
            sendErrorResponse(exchange, "Ocorreu um erro ao processar a sua solicitação HTTP: " + error.getMessage());
        }
    }

    @Override
    public void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseMessage.length());

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseMessage.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    @Override
    public void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException {
        sendResponse(exchange, 500, errorMessage);
    }
}
