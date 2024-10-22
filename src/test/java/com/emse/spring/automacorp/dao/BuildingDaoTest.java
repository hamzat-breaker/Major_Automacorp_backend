package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.HeaterEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BuildingDaoTest {

    @Autowired
    private BuildingDao buildingDao;

    @Test
    public void shouldFindABuildingById() {
        BuildingEntity building = buildingDao.getReferenceById(-10L);
        Assertions.assertThat(building.getOutsideTemperature().getValue()).isEqualTo(1.3);
    }
}
