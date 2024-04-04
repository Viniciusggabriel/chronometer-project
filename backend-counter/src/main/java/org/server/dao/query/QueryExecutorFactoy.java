package org.server.dao.query;

import org.server.dao.dto.SelectResponseOperation;
import org.server.dao.dto.OperationResult;

import java.io.IOException;

interface QueryExecutorSelectFactory {
    SelectResponseOperation executeQuery(String query, String valueColumn) throws IOException;
}

interface QueryExecutorInsertFactory {
    OperationResult executeInsert(String query) throws IOException;
}
