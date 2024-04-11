package org.server.dao.query;

import org.server.dto.SelectResponseOperation;
import org.server.dto.OperationResult;

import java.io.IOException;

interface QueryExecutorSelectFactory {
    SelectResponseOperation executeQuery(String query, String columnTime, String columnLong) throws IOException;
}

interface QueryExecutorInsertFactory {
    OperationResult executeInsert(String query) throws IOException;
}
