package org.telemetry;

import org.telemetry.controller.server.HttpServerTelemetry;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServerTelemetry httpServerTelemetry = new HttpServerTelemetry();
        try {
            httpServerTelemetry.HttpRun();
        } catch (IOException error) {
            Logger logger = Logger.getLogger(IOException.class.getName());
            logger.log(Level.INFO, "Erro subir servidor http: " + error);
        }
    }
}