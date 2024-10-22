package com.emse.spring.automacorp.dto;

public record Heater(Long id, String name, Long power, Sensor heaterStatus, Long roomId) {
}
