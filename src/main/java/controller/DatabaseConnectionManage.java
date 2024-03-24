package controller;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManage  implements DatabaseConnection {
    Dotenv dotenv = Dotenv.load(); // Carrega as variáveis do .env

    private final String url = dotenv.get("DB_URL");
    private final String user = dotenv.get("DB_USER");
    private final String password = dotenv.get("DB_PASSWORD");

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Passa o driver para conexão
            System.out.println("🔥 Conexão ao banco de dados bem sucedida");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException error) {
            System.err.println("Ouve um erro ao se conectar com o MySql:" + error);
            throw new SQLException("Driver JDBC não encontrado", error);
        }
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        try {
            if (connection != null) connection.close();
            System.err.println("❌ Conexão encerrada com o banco de dados");
        } catch (SQLException error) {
            // SQLException exception
            System.err.println("Erro ao encerrar conexão com o banco de dados, ou já foi encerrada: " + error.getMessage());
        }
    }
}
