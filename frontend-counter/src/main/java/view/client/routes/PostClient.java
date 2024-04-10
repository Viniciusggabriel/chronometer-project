package view.client.routes;

import view.client.connection.ClientConnectionFactory;
import view.client.connection.ClientConnectionFactoryManage;
import view.dto.OperationPostClientResult;
import view.dto.OperationResult;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class PostClient implements ClientPost {
    private final ClientConnectionFactory clientConnectionFactoryManage;

    public PostClient(ClientConnectionFactoryManage clientConnectionManage) {
        this.clientConnectionFactoryManage = clientConnectionManage;
    }

    @Override
    public OperationPostClientResult clientPostHandler(String data) throws IOException {
        try {
            HttpURLConnection urlConnection = clientConnectionFactoryManage.getConnection("http://localhost:8080/submit", "POST");
            urlConnection.setDoOutput(true);

            // Escreve os dados no corpo da solicitação
            try (OutputStream outputStream = urlConnection.getOutputStream()) {
                byte[] postDataBytes = data.getBytes(StandardCharsets.UTF_8);
                outputStream.write(postDataBytes);
            } catch (IOException error) {
                OperationResult operationResult = new OperationResult(400, "Erro ao inserir dados dentro da requisição: " + error);
                return new OperationPostClientResult(null, operationResult);
            }

            // Verifica o código de resposta
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                OperationResult operationResult = new OperationResult(responseCode, "Sucesso ao inserir dados");
                return new OperationPostClientResult(data, operationResult);
            } else {
                OperationResult operationResult = new OperationResult(responseCode, "Erro interno do servidor");
                return new OperationPostClientResult(null, operationResult);
            }

        } catch (IOException error) {
            OperationResult operationResult = new OperationResult(504, "Erro ao realizar requisição: " + error);
            return new OperationPostClientResult(null, operationResult);
        }
    }
}
