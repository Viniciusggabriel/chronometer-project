package controller;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Criando handlers para as rotas
        server.createContext("/", new RouteSelectHandler());
        server.createContext("/submit", new RouteSubmitHandler());

        server.start();
    }
}
