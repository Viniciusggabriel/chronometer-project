package org.telemetry.repository.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionFactory {
    Connection getConnection() throws SQLException;

    void closeConnection(Connection connection);
}