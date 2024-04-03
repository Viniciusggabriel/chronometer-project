package org.server.dao.query;

import org.server.dao.connection.DatabaseConnectionFactory;
import org.server.dao.dto.ErrorResponse;
import org.server.dao.dto.ResultSelect;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutorSelect implements QueryExecutorSelectFactory {
    private final DatabaseConnectionFactory connectionManager; // Não pode ser alterado após inicializado

    public QueryExecutorSelect(DatabaseConnectionFactory connectionManager) {
        this.connectionManager = connectionManager;
    } // Pede a conexão do banco de dados como constructor

    @Override
    public Object[] executeQuery(String query, String valueColumn) throws IOException {
        List<Time> resultList = new ArrayList<>(); // Uma list de Time
        List<ErrorResponse> errorResponseList = new ArrayList<>(); // Transforma o erro em uma lista

        // Tenta a conexão com banco de dados
        try (Connection connection = connectionManager.getConnection()) {
            // Tenta criar o cursor dentro do banco de dados e executar a query
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Time valuesResult = resultSet.getTime(valueColumn);
                    resultList.add(valuesResult); // Adiciona dentro da lista de objetos o resultado
                }

                ResultSelect resultSelect = new ResultSelect(resultList.toArray());
                return resultSelect.resultSelect();
            } catch (SQLException error) {
                Logger logger = Logger.getLogger(SQLException.class.getName());
                logger.log(Level.INFO, "Erro ao preparar cursor no banco de dados: " + error);

                ErrorResponse errorResponse = new ErrorResponse(500, "Erro ao buscar informações dentro do banco de dados, tente novamente ou entre em contato com o suporte");

                errorResponseList.add(errorResponse);
                return errorResponseList.toArray(); // Transforma a lista de erro em um array
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(SQLException.class.getName());
            logger.log(Level.INFO, "Erro ao prepara conexão no banco de dados: " + error);

            ErrorResponse errorResponse = new ErrorResponse(500, "Erro ao prepara conexão no banco de dados entre em contato com o suporte");

            errorResponseList.add(errorResponse);
            return errorResponseList.toArray(); // Transforma a lista de erro em um array
        }
    }
}
