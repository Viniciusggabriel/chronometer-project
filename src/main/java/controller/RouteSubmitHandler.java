package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.DatabaseConnectionManage;
import model.QueryExecutorImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Time;

public class RouteSubmitHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Se o método for diferente de POST manda erro
        if (!method.equals("POST")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        InputStream requestBody = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        BufferedReader bufferReader = new BufferedReader(inputStreamReader);
        StringBuilder bodyBuilder = new StringBuilder();

        String line;

        while ((line = bufferReader.readLine()) != null) {
            bodyBuilder.append(line);
        }

        String requestBodyString = bodyBuilder.toString();

        Time data = Time.valueOf(requestBodyString);

        try {
            DatabaseConnectionManage connectionManager = new DatabaseConnectionManage();
            QueryExecutorImpl dataBase = new QueryExecutorImpl(connectionManager);

            dataBase.executeInsert("CALL INSERT_DATA_TIME('" + data + "');");

            String response = "Valor inserido com sucesso";
            exchange.sendResponseHeaders(201, response.length());

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        } catch (SQLException error) {
            System.err.println("Erro ao inserir tempo dentro do banco de dados: " + error);
        }
    }
}
