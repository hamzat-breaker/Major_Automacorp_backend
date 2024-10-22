package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.WindowEntity;

import java.util.List;

public interface WindowDaoCustom {
    List<WindowEntity> findRoomsWithOpenWindows(Long id);

    List<WindowEntity> findWindowsByRoomName(String name);
}
