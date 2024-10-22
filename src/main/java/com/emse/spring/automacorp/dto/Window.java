package com.emse.spring.automacorp.dto;


public record Window(Long id, String name, Sensor windowStatus, Long roomId) {
}
