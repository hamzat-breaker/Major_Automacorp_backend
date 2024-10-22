package com.emse.spring.automacorp.dto;

import java.util.List;

public record Building(Long id, String name, Sensor outsideTemperature, List<Room> rooms) {
}
