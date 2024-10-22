package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class RoomMapperTest {

    @Test
    void shouldMapRoom() {
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

        SensorEntity windowStatus = new SensorEntity(SensorType.STATUS, "Window status");
        windowStatus.setId(2L);
        windowStatus.setValue(0.0);

        WindowEntity windowEntity = new WindowEntity("Window 1", windowStatus, roomEntity);
        windowEntity.setId(2L);
        roomEntity.setWindows(Set.of(windowEntity));

        // Act
        Room room = RoomMapper.of(roomEntity);

        // Assert
        Room expectedRoom = new Room(
                1L,
                "Room",
                1,
                new Sensor(1L, "Room temperature", 24.2, SensorType.TEMPERATURE),
                22.0,
                List.of(new Window(
                        2L,
                        "Window 1",
                        new Sensor(2L, "Window status", 0.0, SensorType.STATUS),
                        1L
                )),
                List.of(),
                1L
        );
        Assertions.assertThat(room).usingRecursiveAssertion().isEqualTo(expectedRoom);
    }
}
