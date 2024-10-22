package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.HeaterEntity;

public class HeaterMapper {
    public static Heater of(HeaterEntity heater) {
        Sensor heaterStatus = new Sensor(
                heater.getHeaterStatus().getId(),
                heater.getHeaterStatus().getName(),
                heater.getHeaterStatus().getValue(),
                heater.getHeaterStatus().getSensorType()
        );

        return new Heater(
                heater.getId(),
                heater.getName(),
                heater.getPower(),
                heaterStatus,
                heater.getRoom().getId()
        );
    }
}
