package org.telemetry.repository.dao.methods;

import org.telemetry.entity.TimeEntity;
import org.telemetry.repository.dao.connection.DataBaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MethodInsert implements MethodInsertQuery {
    private final DataBaseConnection dataBaseConnection;

    public MethodInsert(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @Override
    public boolean executeInsert(TimeEntity time) throws IOException, SQLException {
        try (Connection connection = dataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("CALL P_TELEMETRY_TIME(?,?,?,?)")) {
            try {
                preparedStatement.setInt(1, time.getMilliseconds());
                preparedStatement.setInt(2, time.getSeconds());
                preparedStatement.setInt(3, time.getMinutes());
                preparedStatement.setInt(4, time.getHours());

                preparedStatement.executeUpdate();
                return true;
            } finally {
                dataBaseConnection.closeConnection(connection);
            }
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar conexão no banco de dados: " + error);
        }
    }
}
