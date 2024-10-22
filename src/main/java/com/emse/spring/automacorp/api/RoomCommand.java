package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.HeaterEntity;
import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.WindowEntity;

import java.util.Set;

public record RoomCommand(String name, Integer floor, Double currentTemperature, Double targetTemperature, Long buildingId) {
}
