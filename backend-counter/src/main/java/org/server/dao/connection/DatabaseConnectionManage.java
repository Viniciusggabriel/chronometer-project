package org.server.dao.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManage implements DatabaseConnectionFactory {
    static Dotenv dotenv = Dotenv.load(); // Carrega as variáveis do .env
    private static final DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(dotenv.get("MYSQL_URL"));
        config.setUsername(dotenv.get("MYSQL_USER"));
        config.setPassword(dotenv.get("MYSQL_ROOT_PASSWORD"));

        config.setMaximumPoolSize(10); // Configura tamanho máximo do pool

        dataSource = new HikariDataSource(config); // passa todos os dados para o Hikari realizar o pool
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            System.out.println("🏦 Conexão realizada com sucesso");
            return dataSource.getConnection();
        } catch (SQLException error) {
            throw new SQLException("Ouve um erro ao se conectar com o MySql: " + error.getMessage());
        }
    }

    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("❌ Conexão encerrada");
            } catch (SQLException error) {
                System.err.println("Erro ao fechar o pool com o banco de dados: " + error.getMessage());
            }
        }
    }
}