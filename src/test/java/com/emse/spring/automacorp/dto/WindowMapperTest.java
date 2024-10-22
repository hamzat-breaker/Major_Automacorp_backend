package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class WindowMapperTest {

    @Test
    void shouldMapWindow() {
        // Arrange
        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE, "Room temperature");
        currentTemperature.setId(1L);
        currentTemperature.setValue(24.2);

        SensorEntity outsideTemperature = new SensorEntity(SensorType.TEMPERATURE, "Outside temperature");
        outsideTemperature.setId(3L);
        outsideTemperature.setValue(20.0);

        BuildingEntity buildingEntity = new BuildingEntity("Buiilding 1", outsideTemperature);
        buildingEntity.setId(1L);

        RoomEntity roomEntity = new RoomEntity("Room", currentTemperature, 1, buildingEntity);
        roomEntity.setId(1L);
        roomEntity.setTargetTemperature(22.0);

        SensorEntity windowStatus = new SensorEntity(SensorType.STATUS, "Window status");
        windowStatus.setId(2L);
        windowStatus.setValue(0.0);

        WindowEntity windowEntity = new WindowEntity("Window 1", windowStatus, roomEntity);
        windowEntity.setId(2L);
        roomEntity.setWindows(Set.of(windowEntity));

        // Act
        Window window = WindowMapper.of(windowEntity);

        // Assert
        Window expectedWindow = new Window(
                2L,
                "Window 1",
                new Sensor(2L, "Window status", 0.0, SensorType.STATUS),
                1L
        );
        Assertions.assertThat(window).usingRecursiveAssertion().isEqualTo(expectedWindow);

    }
}
