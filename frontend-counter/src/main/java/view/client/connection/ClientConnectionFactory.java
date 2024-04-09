package view.client.connection;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface ClientConnectionFactory {
    HttpURLConnection getConnection(String urlRoute, String method) throws IOException;

    // Delete:
    void closeConnection(HttpURLConnection connection);
}
