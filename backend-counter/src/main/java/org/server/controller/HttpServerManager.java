package org.server.controller;

import com.sun.net.httpserver.HttpServer;
import org.server.controller.routes.RouteInsertHandler;
import org.server.controller.routes.RouteSelectHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0",8080), 0);

        // Criando as rotas
        server.createContext("/", new RouteSelectHandler());
        server.createContext("/submit", new RouteInsertHandler());

        System.out.println("🔥 Server http rodando");
        server.start();
    }
}