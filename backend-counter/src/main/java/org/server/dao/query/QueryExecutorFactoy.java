package org.server.dao.query;

import org.server.dao.dto.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;

interface QueryExecutorSelectFactory {
    Object[] executeQuery(String query, String valueColumn) throws IOException;
}

interface QueryExecutorInsertFactory {
    ErrorResponse executeInsert(String query) throws IOException;
}
