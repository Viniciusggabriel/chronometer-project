package org.server.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutorSele implements QueryExecutorSelect {
    private final DatabaseConnection connectionManager; // Não pode ser alterado após inicializado

    public QueryExecutorSele(DatabaseConnection connectionManager) {
        this.connectionManager = connectionManager;
    } // Pede a conexão do banco de dados como constructor

    @Override
    public Object[] executeQuery(String query, String valueColumn) throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<Time> resultList = new ArrayList<>(); // Uma list de Time


        // Tenta a conexão com banco de dados
        try {
            assert connection != null; // Caso diferente de null passa se for null retorna erro

            // Tenta criar o cursor dentro do banco de dados e executar a query
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Time valuesResult = resultSet.getTime(valueColumn);
                    resultList.add(valuesResult); // Adiciona dentro da lista de objetos o resultado
                }

                return resultList.toArray();
            } catch (SQLException error) {
                throw new SQLException("Erro ao preparar cursor no banco de dados: " + error.getMessage());
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (SQLException error) {
            throw new SQLException("Ouve um erro ao se conectar com o banco de dados, verifique os parâmetros passados: " + error.getMessage());
        }
    }
}
