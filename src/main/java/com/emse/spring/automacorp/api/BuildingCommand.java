package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.SensorEntity;

import java.util.Set;

public record BuildingCommand(String name, Double outsideTemperature) {
}
