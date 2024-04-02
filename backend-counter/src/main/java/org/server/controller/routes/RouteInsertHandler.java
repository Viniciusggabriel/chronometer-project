package org.server.controller.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.server.dao.connection.DatabaseConnectionManage;
import org.server.dao.query.QueryExecutorInsert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Time;

public class RouteInsertHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Se o método for diferente de POST manda erro
        if (!method.equals("POST")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        // Cria um buffer e passa os valores do body
        InputStream requestBody = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        BufferedReader bufferReader = new BufferedReader(inputStreamReader);
        StringBuilder bodyBuilder = new StringBuilder(); // Cria um stringBuilder

        // Passa os valores do buffer para o line e faz um loop passando os valores da line para a stringBuilder
        String line;
        while ((line = bufferReader.readLine()) != null) {
            bodyBuilder.append(line);
        }

        String requestBodyString = bodyBuilder.toString();

        Time data = Time.valueOf(requestBodyString);

        try {
            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorInsert dataBase = new QueryExecutorInsert(connectionManager);

            dataBase.executeInsert("CALL INSERT_DATA_TIME('" + data + "');");

            String response = "Valor inserido com sucesso";
            exchange.sendResponseHeaders(201, response.length());

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        } catch (SQLException error) {
            // Constrói uma exceção
            throw new RuntimeException("Erro ao inserir valores dentro do banco de dados: " + error.getMessage());
        }
    }
}
