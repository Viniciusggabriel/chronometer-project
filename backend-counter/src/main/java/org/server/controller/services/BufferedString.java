package org.server.controller.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Time;

public class BufferedString implements CreateBufferedTime {
    @Override
    public Time bufferedTime(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(); // String mutável

        /* Loop responsável por inserir os dados do buffer em uma nova variável e depois passar para o StringBuilder, após isso transforma em uma String normal */

        String bufferLine;
        while ((bufferLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(bufferLine);
        }
        String requestBodyString = stringBuilder.toString();

        return Time.valueOf(requestBodyString);
    }
}
