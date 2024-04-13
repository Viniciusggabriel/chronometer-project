package org.telemetry.repository;

import org.telemetry.entity.TimeEntity;
import org.telemetry.repository.dao.methods.MethodInsert;
import org.telemetry.repository.dao.methods.MethodSelect;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TimeRepo {
    private final MethodSelect methodSelect;
    private final MethodInsert methodInsert;

    public TimeRepo(MethodSelect methodSelect, MethodInsert methodInsert) {
        this.methodSelect = methodSelect;
        this.methodInsert = methodInsert;
    }

    // Insere um novo time
    public void addTime(TimeEntity time) throws SQLException, IOException {
        try {
            methodInsert.executeInsert(time);
        } catch (SQLException | IOException error) {
            throw new RuntimeException("Erro ao buscar dados dentro do banco de dados: " + error);
        }
    }

    // Pega um time
    public TimeEntity getTime(TimeEntity time, int indexTime) throws SQLException, IOException {
        try {
            List<TimeEntity> times = methodSelect.executeQuery(time);

            if (!times.isEmpty()) {
                return times.get(indexTime);
            } else {
                throw new RuntimeException("Erro, o banco de dados possui valores vazios");
            }
        } catch (SQLException | IOException error) {
            throw new RuntimeException("Erro ao buscar dados dentro do banco" + error);
        }
    }

}

