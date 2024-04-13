package view.client.routes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import view.client.connection.ClientConnectionFactory;
import view.client.connection.ClientConnectionFactoryManage;
import view.dto.OperationResult;
import view.dto.OperationGetClientResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class GetClient implements ClientGet {
    private final ClientConnectionFactory clientConnectionFactoryManage;

    public GetClient(ClientConnectionFactoryManage clientConnectionManage) {
        this.clientConnectionFactoryManage = clientConnectionManage;
    }

    @Override
    public OperationGetClientResult clientGetHandler() throws IOException {
        try {
            HttpURLConnection urlConnection = clientConnectionFactoryManage.getConnection("http://localhost:8080/select", "GET");

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Monta uma string mutável
                StringBuilder response = new StringBuilder();

                // Loop que pega os valores de dentro do buffer e passa para o line e depois para a string
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // Encerra os dados recebidos do servidor
                reader.close();

                String jsonResponse = response.toString();
                Gson gson = new Gson();

                clientConnectionFactoryManage.closeConnection(urlConnection);

                // DTO
                // fromJson funciona pegando a String json, o modo que você quer no caso qualquer um com um mapa dentro chave String: Valor Objeto
                return new OperationGetClientResult(gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>() {
                }.getType()), null);
            } else {
                OperationResult operationResult = new OperationResult(responseCode, "Erro interno do servidor");
                return new OperationGetClientResult(null, operationResult);
            }

        } catch (IOException error) {
            OperationResult operationResult = new OperationResult(504, "Erro ao realizar requisição: " + error);
            return new OperationGetClientResult(null, operationResult);
        }
    }
}
