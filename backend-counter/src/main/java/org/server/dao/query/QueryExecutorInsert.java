package org.server.dao.query;

import org.server.dao.connection.DatabaseConnectionFactory;
import org.server.dao.dto.OperationResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutorInsert implements QueryExecutorInsertFactory {
    private final DatabaseConnectionFactory connectionManager;

    // Pede a conexão do banco de dados como constructor
    public QueryExecutorInsert(DatabaseConnectionFactory connectionManager) {
        this.connectionManager = connectionManager;// O "this." é usado para pegar a variável da instância
    }

    @Override
    public OperationResult executeInsert(String query) throws IOException {

        /* Info: tratamento de erros
         * Tratamento de erros caso a conexão não execute
         * Tratamento de erros caso o statement não execute de forma certa ou os dados foram inseridos errados
         * Imprime no console a gravidade do erro, mensagem personalizada + erro
         * Retorna um státus para o http e uma mensagem de erro ou sucesso
         * */

        try (Connection connection = connectionManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                return new OperationResult(201, "Criado com sucesso");
            } catch (SQLException error) {
                Logger logger = Logger.getLogger(SQLException.class.getName());
                logger.log(Level.INFO, "Erro ao preparar o statement dentro do banco de dados, ou dados inseridos de forma errada: " + error);

                return new OperationResult(500, "Erro ao inserir seus dados, verifique novamente se segue o padrão 00:00:00");
            } finally {
                connectionManager.closeConnection(connection);
            }
        } catch (SQLException error) {
            Logger logger = Logger.getLogger(SQLException.class.getName());
            logger.log(Level.INFO, "Erro ao prepara conexão no banco de dados: " + error);

            return new OperationResult(500, "Erro ao prepara conexão no banco de dados entre em contato com o suporte");
        }
    }
}