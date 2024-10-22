package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.*;
import com.emse.spring.automacorp.dto.Room;
import com.emse.spring.automacorp.dto.RoomMapper;
import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.RoomEntity;
import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/** This API serves to manipulate rooms.
 * @author Céline Ni
 * @version 2.3
 */

@CrossOrigin
@RestController
@RequestMapping("/api/rooms")
@Transactional
public class RoomController {

    private final RoomDao roomDao;
    private final WindowDao windowDao;
    private final HeaterDao heaterDao;
    private final BuildingDao buildingDao;
    private final SensorDao sensorDao;

    private static Logger logger = LogManager.getLogger(RoomController.class);

    public RoomController(RoomDao roomDao, WindowDao windowDao, HeaterDao heaterDao, BuildingDao buildingDao, SensorDao sensorDao) {
        this.roomDao = roomDao;
        this.windowDao = windowDao;
        this.heaterDao = heaterDao;
        this.buildingDao = buildingDao;
        this.sensorDao = sensorDao;
    }

    /**
     * Often the first function called during the acces to this API
     * @return List of all rooms
     */
    @GetMapping
    public List<Room> findAll() {
        return roomDao.findAll()
                .stream()
                .map(RoomMapper::of)
                .sorted(Comparator.comparing(Room::name))
                .collect(Collectors.toList());
    }

    /**
     * This function returns the room associated to the given room_id
     * @param room_id
     * @return room
     */
    @GetMapping(path = "/{id}")
    public Room findById(@PathVariable Long id) {
        return roomDao.findById(id).map(RoomMapper::of).orElse(null);
    }

    /**
     * Create a room by using the information send by the frontend. A "current temperature" sensor also is created
     * during this process using the value of currentTemperature.
     * @param RoomCommand (String name, Integer floor, Double currentTemperature, Double targetTemperature, Long buildingId)
     * @return the server response of the request (200 : everything is fine, 401 error,...)
     */
    @PostMapping
    public ResponseEntity<Room> create(@RequestBody RoomCommand room) {
        BuildingEntity building = buildingDao.findById(room.buildingId()).orElse(null);
        if (building==null){
            return ResponseEntity.badRequest().build();
        }

        SensorEntity currentTemperature = new SensorEntity(SensorType.TEMPERATURE,room.name() + " current temperature");
        currentTemperature.setValue(room.currentTemperature());
        sensorDao.save(currentTemperature);

        RoomEntity entity = new RoomEntity(room.floor(), room.name(), currentTemperature, room.targetTemperature() , building);
        RoomEntity saved = roomDao.save(entity);
        return ResponseEntity.ok(RoomMapper.of(saved));
    }

    /**
     * Modify a room information using its id and new information
     * @param room_id, RoomCommand
     * @return the server response of the request (200 : everything is fine, 401 error,...)
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id, @RequestBody RoomCommand room) {
        if (id==null && room==null){
            logger.fatal("There are no id nor room...");
            return ResponseEntity.badRequest().build();
        }
        else if (id==null || room==null) {
            logger.error("There is a missing argument between id and room.");
            return ResponseEntity.badRequest().build();
        }
        else {
            RoomEntity entity = roomDao.findById(id).orElse(null);
            if (entity == null) {
                logger.warn("The given room id (" + id +")doesn't exist.");
                return ResponseEntity.badRequest().build();
            }
            logger.debug("Beginning of room " + id + "'s update");

            SensorEntity currentTemperature = sensorDao.findById(entity.getCurrentTemperature().getId()).orElse(null);
            if (currentTemperature == null) {
                return ResponseEntity.badRequest().build();
            }
            currentTemperature.setValue(room.currentTemperature());

            entity.setFloor(room.floor());
            entity.setName(room.name());
            entity.setTargetTemperature(room.targetTemperature());
            logger.info("The room " + id + " has successfully been modified.");
            return ResponseEntity.ok(RoomMapper.of(entity));
        }
    }

    /**
     * Delete a room, its windows, heaters and associated sensors using the room_id
     * @param room_id
     */
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        sensorDao.deleteByRoomId(id);
        windowDao.deleteWindowsByRoomId(id);
        heaterDao.deleteHeatersByRoomId(id);
        roomDao.deleteById(id);
    }

    /**
     * Open all the windows of a room using its id
     * @param room_id
     * @return the server response of the request (200 : everything is fine, 401 error,...)
     */
    @PutMapping(path = "/{id}/openWindows")
    public ResponseEntity<Room> openWindows(@PathVariable Long id) {
        RoomEntity entity = roomDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        windowDao.updateWindowStatusByRoomId(id, 1.0);
        entity.setWindows(roomDao.findById(id).get().getWindows());
        return ResponseEntity.ok(RoomMapper.of(entity));
    }

    /**
     * Close all the windows of a room using its id
     * @param room_id
     * @return the server response of the request (200 : everything is fine, 401 error,...)
     */
    @PutMapping(path = "/{id}/closeWindows")
    public ResponseEntity<Room> closeWindows(@PathVariable Long id) {
        RoomEntity entity = roomDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        windowDao.updateWindowStatusByRoomId(id, 0.0);
        entity.setWindows(roomDao.findById(id).get().getWindows());
        return ResponseEntity.ok(RoomMapper.of(entity));
    }

}
