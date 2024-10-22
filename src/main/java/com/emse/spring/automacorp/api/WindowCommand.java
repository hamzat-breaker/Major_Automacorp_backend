package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.SensorEntity;

public record WindowCommand(String name, Double windowStatus, Long roomId) {
}
