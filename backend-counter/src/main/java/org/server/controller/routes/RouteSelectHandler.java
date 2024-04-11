package org.server.controller.routes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.services.SumTimesString;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dto.SelectResponseOperation;
import org.server.dto.OperationResult;
import org.server.dao.query.QueryExecutorSelect;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

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

        SelectResponseOperation responseQuery = null;
        try {
            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorSelect dataBase = new QueryExecutorSelect(connectionManager);

            responseQuery = dataBase.executeQuery("SELECT * FROM TIME_CLOCK ORDER BY ID_TIME DESC LIMIT 2", "TIME_LAP", "TIME_MILLISECONDS");

            // Verificar se a resposta não é nula
            if (responseQuery.selectResponse() != null) {
                List<Time> resultTimeList = responseQuery.selectResponse().resultTime();
                List<Long> resultMillisecondsList = responseQuery.selectResponse().resultMilliseconds();

                // Verificar se as listas não são nulas e têm o mesmo tamanho
                if (resultTimeList != null && resultMillisecondsList != null && resultTimeList.size() == resultMillisecondsList.size()) {

                    Time firstTime = resultTimeList.get(0);
                    Time secondTime = resultTimeList.get(1);
                    
                    // Todo: fazer a soma de milissegundos
                    Long firstMilliseconds = resultMillisecondsList.get(0);
                    Long secondMilliseconds = resultMillisecondsList.get(1);


                    SumTimesString sumTimesString = new SumTimesString();

                    try {
                        sendResponse(exchange, 200, sumTimesString.twoTimeSum(firstTime, secondTime));
                    } catch (IOException error) {
                        sendResponse(exchange, 102, "Processando");
                    }
                } else {
                    // Caso as listas forem nulas
                    sendErrorResponse(exchange, "Listas de valores vazias ou de tamanhos diferentes");
                }
            } else {
                sendErrorResponse(exchange, "Requisição vazia");
            }
        } catch (IOException error) {
            if (responseQuery != null) {
                OperationResult operationResult = responseQuery.operationResult();

                // verifica se a resposta de erro é do banco de dados e retorna ela ou retorna o erro do HTTP
                if (operationResult.operationCode() == 500) {
                    sendErrorResponse(exchange, operationResult.operationMessage());
                } else {
                    sendErrorResponse(exchange, operationResult.operationMessage() + error.getMessage());
                }
            } else {
                // Caso o resultSelectTimer
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