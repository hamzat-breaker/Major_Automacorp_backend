package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BuildingMapperTest {

    @Test
    void shouldMapBuilding() {
        // Arrange
        SensorEntity outsideTemperature = new SensorEntity(SensorType.TEMPERATURE, "Outside temperature");
        outsideTemperature.setId(3L);
        outsideTemperature.setValue(20.0);

        BuildingEntity buildingEntity = new BuildingEntity("Building 1", outsideTemperature);
        buildingEntity.setId(1L);

        // Act
        Building building = BuildingMapper.of(buildingEntity);

        // Assert
        Building expectedBuilding = new Building(
                1L,
                "Building 1",
                new Sensor(3L, "Outside temperature", 20.0, SensorType.TEMPERATURE),
                new ArrayList<>()
        );
        Assertions.assertThat(building).usingRecursiveAssertion().isEqualTo(expectedBuilding);

    }
}
