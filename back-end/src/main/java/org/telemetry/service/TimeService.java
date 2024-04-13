package org.telemetry.service;

import org.telemetry.dto.TimeDto;
import org.telemetry.entity.TimeEntity;
import org.telemetry.repository.TimeRepo;
import org.telemetry.repository.dao.connection.DataBaseConnection;
import org.telemetry.repository.dao.methods.MethodInsert;
import org.telemetry.repository.dao.methods.MethodSelect;

import java.io.IOException;
import java.sql.SQLException;

public class TimeService implements TimeServiceFactory {
    private final TimeRepo timeRepo;

    public TimeService() {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        MethodSelect methodSelect = new MethodSelect(dataBaseConnection);
        MethodInsert methodInsert = new MethodInsert(dataBaseConnection);

        this.timeRepo = new TimeRepo(methodSelect, methodInsert);
    }

    @Override
    public boolean addTimeDto(TimeDto timeDto) {
        try {
            TimeEntity time = new TimeEntity(timeDto.milliseconds(), timeDto.seconds(), timeDto.minutes(), timeDto.hours());
            // Todo: validar dados
            timeRepo.addTime(time);
            return true;
        } catch (SQLException error) {
            throw new RuntimeException("Erro ao inserir dentro do banco de dados: " + error);
        } catch (IOException error) {
            throw new RuntimeException("Erro de E/S dentro do banco de dados:  " + error);
        }
    }

    @Override
    public TimeDto getTimeDto(TimeDto timeDto, int indexTime) {
        try {
            TimeEntity time = new TimeEntity(timeDto.milliseconds(), timeDto.seconds(), timeDto.minutes(), timeDto.hours());
            // Todo: validar dados
            TimeEntity timeResult = timeRepo.getTime(time, indexTime);

            if (timeResult == null) {
                return new TimeDto(0, 0, 0, 0);
            } else {
                return new TimeDto(timeResult.getMilliseconds(), timeResult.getSeconds(), timeResult.getMinutes(), timeResult.getHours());
            }
        } catch (SQLException error) {
            throw new RuntimeException("Erro ao inserir dentro do banco de dados: " + error);
        } catch (IOException error) {
            throw new RuntimeException("Erro de E/S dentro do banco de dados:  " + error);
        }
    }
}
