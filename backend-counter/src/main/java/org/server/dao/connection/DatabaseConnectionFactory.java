package org.server.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionFactory {
    Connection getConnection() throws SQLException;
    void closeConnection(Connection connection);
}