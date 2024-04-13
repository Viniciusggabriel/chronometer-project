package org.telemetry.repository.dao.methods;

import org.telemetry.entity.TimeEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

interface MethodSelectQuery {
    List<TimeEntity> executeQuery(TimeEntity time) throws IOException, SQLException;
}

interface MethodInsertQuery {
    boolean executeInsert(TimeEntity time) throws IOException, SQLException;
}