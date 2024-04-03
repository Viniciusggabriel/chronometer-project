package org.server.dao.query;

import org.server.dao.connection.DatabaseConnectionFactory;
import org.server.dao.dto.ErrorResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutorInsert implements QueryExecutorInsertFactory {
    private final DatabaseConnectionFactory connectionManager;

    // Pede a conexão do banco de dados como constructor
    public QueryExecutorInsert(DatabaseConnectionFactory connectionManager) {
        this.connectionManager = connectionManager;// O "this." é usado para pegar a variável da instância
    }

    @Override
    public ErrorResponse executeInsert(String query) throws SQLException {
        Connection connection = connectionManager.getConnection();

        try {
            assert connection != null; // Caso diferente de null passa e se for null retorna erro
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                return new ErrorResponse(201, "Dado criado dentro do banco de dados");
            } catch (SQLException error) {
                // Mostra os erros ao preparar o statement
                return new ErrorResponse(204, "Erro ao prepara statement dentro do banco de dados" + error.getMessage());
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (Exception error) {
            // Mostra os erros da conexão do banco de dados
            return new ErrorResponse(204, "Erro ao prepara conexão no banco de dados: " + error.getMessage());
        }
    }
}