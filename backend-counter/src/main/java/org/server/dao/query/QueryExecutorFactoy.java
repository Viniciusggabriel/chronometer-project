package org.server.dao.query;

import org.server.dao.dto.ErrorResponse;

import java.sql.SQLException;

interface QueryExecutorSelectFactory {
    Object[] executeQuery(String query, String valueColumn) throws SQLException;
}

interface QueryExecutorInsertFactory {
    ErrorResponse executeInsert(String query) throws SQLException;
}
