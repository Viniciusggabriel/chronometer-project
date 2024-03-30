package org.server.http;

import com.sun.net.httpserver.HttpServer;
import org.server.http.routes.RouteSelectHandler;
import org.server.http.routes.RouteSubmitHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0",8080), 0);

        // Criando handlers para as rotas
        server.createContext("/", new RouteSelectHandler());
        server.createContext("/submit", new RouteSubmitHandler());

        System.out.println("🔥 Server http rodando");
        server.start();
    }
}