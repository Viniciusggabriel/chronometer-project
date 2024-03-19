package controller;

import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    Dotenv dotenv = Dotenv.load(); // Carrega as variáveis do .env
    private final String url = dotenv.get("DB_URL");
    private final String user = dotenv.get("DB_USER");
    private final String password = dotenv.get("DB_PASSWORD");

    // Método de conexão ao Banco de dados
    public static Connection dataBaseConnection(String url, String user, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Passa o driver para conexão
            System.out.println("🔥 Conexão ao banco de dados bem sucedida");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException error) {
            System.err.println("Ouve um erro ao se conectar com o MySql:" + error);
            throw new SQLException("Driver JDBC não encontrado", error);
        }
    }

    public static void dataBaseCloseConnection(Connection connection) {
        try {
            if (connection != null) connection.close();
            System.err.println("❌ Conexão encerrada com o banco de dados");
        } catch (SQLException error) {
            // SQLException exception
            System.err.println("Erro ao encerrar conexão com o banco de dados, ou já foi encerrada: " + error.getMessage());
        }
    }

    // Método de select, com where dentro do banco de dados
    public Object[] querySelectDataBase(String query, String valueColumn) {
        List<Object> resultList = new ArrayList<>(); // Recebe qualquer tipo de valor

        // Tenta a conexão com banco de dados
        try (Connection connection = dataBaseConnection(url, user, password)) {
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
                System.err.println("Erro ao preparar cursor no banco de dados: " + error.getMessage());
                return null;
            } finally {
                dataBaseCloseConnection(connection);
            }
        } catch (SQLException error) {
            System.err.println("Ouve um erro ao se conectar com o banco de dados, verifique os parâmetros passados: " + error.getMessage());
            return null;
        }
    }


    public void queryInsertDataBase(Time data) {
        try (Connection connection = dataBaseConnection(url, user, password)) {
            assert connection != null; // Caso diferente de null passa se for null retorna erro
            try (Statement statement = connection.createStatement();) {

                String query = "CALL INSERT_DATA_TIME('" + data + "');";
                statement.executeUpdate(query);

                ImageIcon icon = new ImageIcon("src/img/check-check.png");
                JOptionPane.showMessageDialog(null, "Tempo inserido com sucesso", "Sucesso", JOptionPane.PLAIN_MESSAGE, icon);
            } catch (SQLException error) {
                System.err.println("Erro ao preparar cursor no banco de dados: " + error.getMessage());
            } finally {
                dataBaseCloseConnection(connection);
            }
        } catch (SQLException error) {
            System.err.println("Erro ao prepara cursor no banco de dados: " + error);
        }
    }
}