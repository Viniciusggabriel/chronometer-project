package org.telemetry.repository.dao.methods;

import org.telemetry.entity.TimeEntity;
import org.telemetry.repository.dao.connection.DataBaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MethodSelect implements MethodSelectQuery {
    private final DataBaseConnection dataBaseConnection;

    public MethodSelect(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @Override
    public List<TimeEntity> executeQuery(TimeEntity time) throws IOException, SQLException {
        List<TimeEntity> times = new ArrayList<>();

        try (Connection connection = dataBaseConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM TELEMETRY_TIME ORDER BY ID_TIME DESC LIMIT 0 , 2;");
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int milliseconds = resultSet.getInt("milliseconds");
                    int seconds = resultSet.getInt("seconds");
                    int minutes = resultSet.getInt("minutes");
                    int hours = resultSet.getInt("hours");
                    time = new TimeEntity(milliseconds, seconds, minutes, hours);
                    times.add(time);
                }

            } catch (SQLException error) {
                throw new RuntimeException("Erro ao preparar statement: " + error);
            } finally {
                dataBaseConnection.closeConnection(connection);
            }

            return times;
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar conexão no banco de dados: " + error);
        }
    }
}
