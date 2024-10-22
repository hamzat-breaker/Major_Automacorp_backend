package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.SensorEntity;

public record HeaterCommand(String name, Long power, Double heaterStatus, Long roomId) {
}
