package org.telemetry.controller.server;

import com.sun.net.httpserver.HttpServer;
import org.telemetry.controller.server.routers.HttpGetRoute;
import org.telemetry.controller.server.routers.HttpPostRoute;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerTelemetry {
    public void HttpRun() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        // Criando as rotas
        server.createContext("/select", new HttpGetRoute());
        server.createContext("/submit", new HttpPostRoute());

        System.out.println("🔥 Server http rodando");
        server.start();
    }
}
