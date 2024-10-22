package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.HeaterEntity;
import com.emse.spring.automacorp.model.RoomEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
public class HeaterDaoTest {

    @Autowired
    private HeaterDao heaterDao;
    @Autowired
    private RoomDao roomDao;

    @Test
    public void shouldFindAHeaterById() {
        HeaterEntity heater = heaterDao.getReferenceById(-10L);
        Assertions.assertThat(heater.getName()).isEqualTo("Heater 1");
    }

    @Test
    public void shouldDeleteHeatersRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getHeaters().stream().map(HeaterEntity::getId).collect(Collectors.toList());
        Assertions.assertThat(roomIds).hasSize(1);

        heaterDao.deleteHeatersByRoomId(-10L);
        List<HeaterEntity> result = heaterDao.findAllById(roomIds);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldOpenHeatersInThisRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getHeaters().stream().map(HeaterEntity::getId).collect(Collectors.toList());

        heaterDao.updateHeaterStatusByRoomId(-10L, 0.0);
        List<HeaterEntity> result = heaterDao.findAllById(roomIds);
        Assertions.assertThat(result)
                .hasSize(1)
                .extracting("id", "name", "heaterStatus.value")
                .containsExactly(Tuple.tuple(-10L, "Heater 1", 0.0));
    }

    @Test
    public void shouldCloseHeatersInThisRoom() {
        RoomEntity room = roomDao.getReferenceById(-9L);
        List<Long> roomIds = room.getHeaters().stream().map(HeaterEntity::getId).collect(Collectors.toList());

        heaterDao.updateHeaterStatusByRoomId(-9L, 1.0);
        List<HeaterEntity> result = heaterDao.findAllById(roomIds);
        Assertions.assertThat(result)
                .hasSize(1)
                .extracting("id", "name", "heaterStatus.value")
                .containsExactly(Tuple.tuple(-9L, "Heater 1", 1.0));
    }
}
