package view.client.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientConnectionFactoryManage implements ClientConnectionFactory {
    @Override
    public HttpURLConnection getConnection(String urlRoute, String method) throws IOException {
        URL urlConnection = new URL(urlRoute);
        HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(3000);

        return connection;
    }

    // Delete:
    @Override
    public void closeConnection(HttpURLConnection connection) {
        connection.disconnect();
    }
}
