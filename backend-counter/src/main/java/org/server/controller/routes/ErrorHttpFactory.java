package org.server.controller.routes;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

interface ErrorHttpFactory {
    void sendResponse(HttpExchange exchange, int statusCode, String responseMessage) throws IOException;


    void sendErrorResponse(HttpExchange exchange, String errorMessage) throws IOException;

}
