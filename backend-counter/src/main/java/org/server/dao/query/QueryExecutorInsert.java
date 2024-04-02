package org.server.dao.query;

import org.server.dao.connection.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutorInsert implements QueryExecutorInsertFactory {
    private final DatabaseConnectionFactory connectionManager;

    public QueryExecutorInsert(DatabaseConnectionFactory connectionManager) {
        this.connectionManager = connectionManager;
    }// Pede a conexão do banco de dados como constructor

    @Override
    public void executeInsert(String query) throws SQLException {
        Connection connection = connectionManager.getConnection();

        try {
            assert connection != null; // Caso diferente de null passa se for null retorna erro
            try (Statement statement = connection.createStatement();) {

                statement.executeUpdate(query);

                System.out.println("Valor inserido com sucesso: " + query);
            } catch (SQLException error) {
                // Trata erros do statement
                System.err.println("Erro ao preparar statement no banco de dados para inserir seu valor: " + error.getMessage());
                error.printStackTrace();
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (Exception error) {
            // Trata erros na conexão
            System.err.println("Erro ao prepara conexão no banco de dados: " + error.getMessage());
            error.printStackTrace();
        }
    }
}