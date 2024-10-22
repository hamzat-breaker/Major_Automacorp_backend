package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.WindowEntity;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
class WindowDaoTest {

    @Autowired
    private WindowDao windowDao;
    @Autowired
    private RoomDao roomDao;

    @Test
    public void shouldFindAWindowById() {
        WindowEntity window = windowDao.getReferenceById(-10L);
        Assertions.assertThat(window.getName()).isEqualTo("Window 1");
        Assertions.assertThat(window.getWindowStatus().getValue()).isEqualTo(1.0);
    }

    @Test
    public void shouldFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-10L);
        Assertions.assertThat(result)
                .hasSize(1)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple(-10L, "Window 1"));
    }

    @Test
    public void shouldNotFindRoomsWithOpenWindows() {
        List<WindowEntity> result = windowDao.findRoomsWithOpenWindows(-9L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindWindowsInThisRoom() {
        List<WindowEntity> result = windowDao.findWindowsByRoomName("Room1");
        Assertions.assertThat(result)
                .hasSize(2)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple(-10L, "Window 1"), Tuple.tuple(-9L, "Window 2"));
    }

    @Test
    public void shouldNotFindWindowsInThisRoom() {
        List<WindowEntity> result = windowDao.findWindowsByRoomName("Room3");
        Assertions.assertThat(result)
                .hasSize(0);
    }

    @Test
    public void shouldDeleteWindowsRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getWindows().stream().map(WindowEntity::getId).collect(Collectors.toList());
        Assertions.assertThat(roomIds).hasSize(2);

        windowDao.deleteWindowsByRoomId(-10L);
        List<WindowEntity> result = windowDao.findAllById(roomIds);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void shouldCloseWindowsInThisRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getWindows().stream().map(WindowEntity::getId).collect(Collectors.toList());

        windowDao.updateWindowStatusByRoomId(-10L, 0.0);
        List<WindowEntity> result = windowDao.findAllById(roomIds);
        Assertions.assertThat(result)
                .hasSize(2)
                .extracting("id", "name", "windowStatus.value")
                .containsExactly(Tuple.tuple(-10L, "Window 1", 0.0), Tuple.tuple(-9L, "Window 2", 0.0));
    }

    @Test
    public void shouldOpenWindowsInThisRoom() {
        RoomEntity room = roomDao.getReferenceById(-10L);
        List<Long> roomIds = room.getWindows().stream().map(WindowEntity::getId).collect(Collectors.toList());

        windowDao.updateWindowStatusByRoomId(-10L, 1.0);
        List<WindowEntity> result = windowDao.findAllById(roomIds);
        Assertions.assertThat(result)
                .hasSize(2)
                .extracting("id", "name", "windowStatus.value")
                .containsExactly(Tuple.tuple(-10L, "Window 1", 1.0), Tuple.tuple(-9L, "Window 2", 1.0));
    }

}
