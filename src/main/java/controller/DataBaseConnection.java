package controller;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    Dotenv dotenv = Dotenv.load(); // Carrega as variáveis do .env
    private final String url = dotenv.get("DB_URL");
    private final String user = dotenv.get("DB_USER");
    private final String password = dotenv.get("DB_PASSWORD");

    // Método de conexão ao DB
    public Connection dataBaseConnection() {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            System.out.println("🔥 Conexão ao banco de dados bem sucedida");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception error) {
            System.out.println("Ouve um erro ao se conectar com o MySql:" + error);
            return null;
        }
    }
}
