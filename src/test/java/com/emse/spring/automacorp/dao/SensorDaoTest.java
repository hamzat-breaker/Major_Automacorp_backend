package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SensorDaoTest {

    @Autowired
    private SensorDao sensorDao;

    @Test
    public void shouldFindASensorById() {
        SensorEntity sensor = sensorDao.getReferenceById(-10L);
        Assertions.assertThat(sensor.getName()).isEqualTo("Temperature room 2");
        Assertions.assertThat(sensor.getValue()).isEqualTo(21.3);
        Assertions.assertThat(sensor.getSensorType()).isEqualTo(SensorType.TEMPERATURE);
    }
}
