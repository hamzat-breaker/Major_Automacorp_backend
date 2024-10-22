package com.emse.spring.automacorp.dto;

import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.WindowEntity;

public class WindowMapper {
    public static Window of(WindowEntity window) {
        Sensor windowStatus = new Sensor(
                window.getWindowStatus().getId(),
                window.getWindowStatus().getName(),
                window.getWindowStatus().getValue(),
                window.getWindowStatus().getSensorType()
        );

        return new Window(
                window.getId(),
                window.getName(),
                windowStatus,
                window.getRoom().getId()
        );
    }
}
