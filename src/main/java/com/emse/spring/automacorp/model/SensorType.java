package com.emse.spring.automacorp.model;

public enum SensorType { TEMPERATURE, POWER, STATUS }
/**
 * This class lists the type of sensor that exist : TEMPERATURE for a temperature sensor,
 *  POWER for a heater and STATUS for a window.
 * By convention if the SensorType is POWER, the value 0.0 of the SensorEntity means the heater is 'off' and 1.0, 'on'.
 * By convention if the SensorType is STATUS, the value 0.0 of the SensorEntity means the window is 'closed' and 1.0, 'open'.
 */
