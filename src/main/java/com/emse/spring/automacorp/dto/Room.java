package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.WindowEntity;

import java.util.List;
import java.util.Set;

public record Room(Long id, String name, Integer floor, Sensor currentTemperature, Double targetTemperature, List<Window> windows, List<Heater> heaters, Long building_id) {
}
