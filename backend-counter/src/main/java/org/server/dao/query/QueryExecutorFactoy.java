package org.server.dao.query;

import java.sql.SQLException;

interface QueryExecutorSelectFactory {
    Object[] executeQuery(String query, String valueColumn) throws SQLException;
}

interface QueryExecutorInsertFactory {
    void executeInsert(String query) throws SQLException;
}
