package com.emse.spring.automacorp.dto;


import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.HeaterEntity;
import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.WindowEntity;

import java.util.ArrayList;
import java.util.List;

public class BuildingMapper {
    public static Building of(BuildingEntity building){

        //define the Sensor outsideTemperature from RoomEntity room data
        Sensor outsideTemperature = new Sensor(
                building.getOutsideTemperature().getId(),
                building.getOutsideTemperature().getName(),
                building.getOutsideTemperature().getValue(),
                building.getOutsideTemperature().getSensorType()
        );

        //define the List<Room> rooms from RoomEntity room data
        List<Room> rooms = new ArrayList<>();
        for (RoomEntity roomEntity : building.getRooms()) {

            Sensor currentTemperature = new Sensor(
                    roomEntity.getCurrentTemperature().getId(),
                    roomEntity.getCurrentTemperature().getName(),
                    roomEntity.getCurrentTemperature().getValue(),
                    roomEntity.getCurrentTemperature().getSensorType());

            List<Window> windows = new ArrayList<>();
            for (WindowEntity windowEntity : roomEntity.getWindows()) {
                Sensor windowStatus = new Sensor(
                        windowEntity.getWindowStatus().getId(),
                        windowEntity.getWindowStatus().getName(),
                        windowEntity.getWindowStatus().getValue(),
                        windowEntity.getWindowStatus().getSensorType());
                Window window = new Window(windowEntity.getId(), windowEntity.getName(), windowStatus, windowEntity.getRoom().getId());
                windows.add(window);
            }

            List<Heater> heaters = new ArrayList<>();
            for (HeaterEntity heaterEntity : roomEntity.getHeaters()) {
                Sensor heaterStatus = new Sensor(
                        heaterEntity.getHeaterStatus().getId(),
                        heaterEntity.getHeaterStatus().getName(),
                        heaterEntity.getHeaterStatus().getValue(),
                        heaterEntity.getHeaterStatus().getSensorType());
                Heater heater = new Heater(heaterEntity.getId(), heaterEntity.getName(), heaterEntity.getPower(),heaterStatus, heaterEntity.getRoom().getId());
                heaters.add(heater);
            }

            Room room = new Room(roomEntity.getId(), roomEntity.getName(), roomEntity.getFloor(), currentTemperature, roomEntity.getTargetTemperature(), windows, heaters, roomEntity.getBuilding().getId());
            rooms.add(room);
        }
        
        return new Building(
                building.getId(),
                building.getName(),
                outsideTemperature,
                rooms
        );
    }
}
