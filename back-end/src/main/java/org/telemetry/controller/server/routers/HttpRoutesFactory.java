package org.telemetry.controller.server.routers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface HttpRoutesFactory {
    void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException;

    void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException;

}
