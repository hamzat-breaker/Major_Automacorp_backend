package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class HeaterMapperTest {
    
    @Test
    void shouldMapHeater() {
        // Arrange
        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE, "Room temperature");
        currentTemperature.setId(1L);
        currentTemperature.setValue(24.2);

        SensorEntity outsideTemperature = new SensorEntity(SensorType.TEMPERATURE, "Outside temperature");
        outsideTemperature.setId(3L);
        outsideTemperature.setValue(20.0);

        BuildingEntity buildingEntity = new BuildingEntity("Building 1", outsideTemperature);
        buildingEntity.setId(1L);

        RoomEntity roomEntity = new RoomEntity("Room", currentTemperature, 1, buildingEntity);
        roomEntity.setId(1L);
        roomEntity.setTargetTemperature(22.0);

        SensorEntity heaterStatus = new SensorEntity(SensorType.POWER, "Heater status");
        heaterStatus.setId(2L);
        heaterStatus.setValue(0.0);

        HeaterEntity heaterEntity = new HeaterEntity("Heater 1", heaterStatus, roomEntity);
        heaterEntity.setId(2L);
        roomEntity.setHeaters(Set.of(heaterEntity));

        // Act
        Heater heater = HeaterMapper.of(heaterEntity);

        // Assert
        Heater expectedHeater = new Heater(
                2L,
                "Heater 1",
                null,
                new Sensor(2L, "Heater status", 0.0, SensorType.POWER),
                1L
        );
        Assertions.assertThat(heater).usingRecursiveAssertion().isEqualTo(expectedHeater);

    }
}
