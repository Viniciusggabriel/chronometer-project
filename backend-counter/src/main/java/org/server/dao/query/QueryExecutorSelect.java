package org.server.dao.query;

import org.server.dao.connection.DatabaseConnectionFactory;
import org.server.dto.SelectResponseOperation;
import org.server.dto.OperationResult;
import org.server.dto.OperationResponse;

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
    public SelectResponseOperation executeQuery(String query, String valueColumn) throws IOException {

        /* Info: tratamento de erros
         * Cria uma lista para os resultados
         * Tratamento de erros caso a conexão não execute
         * Tratamento de erros caso o statement não execute de forma certa ou os dados foram recebidos de forma errada
         * Imprime no console a gravidade do erro, mensagem personalizada + erro
         * Retorna um státus para o http e uma mensagem de erro ou sucesso
         * */

        List<Time> resultList = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                // Adiciona dentro da lista de objetos os resultados
                while (resultSet.next()) {
                    Time valuesResult = resultSet.getTime(valueColumn);
                    resultList.add(valuesResult);
                }

                OperationResponse operationResponse = new OperationResponse(resultList.toArray());

                return new SelectResponseOperation(operationResponse, null);
            } catch (SQLException error) {
                Logger logger = Logger.getLogger(SQLException.class.getName());
                logger.log(Level.INFO, "Erro ao preparar statement no banco de dados: " + error);

                OperationResult operationResult = new OperationResult(500, "Erro ao buscar informações dentro do banco de dados, tente novamente ou entre em contato com o suporte");

                return new SelectResponseOperation(null, operationResult);
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(SQLException.class.getName());
            logger.log(Level.INFO, "Erro ao prepara conexão no banco de dados: " + error);

            OperationResult operationResult = new OperationResult(500, "Erro ao prepara conexão no banco de dados entre em contato com o suporte");

            return new SelectResponseOperation(null, operationResult);
        }
    }
}
