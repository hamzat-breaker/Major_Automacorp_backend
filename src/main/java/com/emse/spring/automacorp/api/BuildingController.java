package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.*;
import com.emse.spring.automacorp.dto.Building;
import com.emse.spring.automacorp.dto.BuildingMapper;
import com.emse.spring.automacorp.model.BuildingEntity;
import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.SensorType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/buildings")
@Transactional
public class BuildingController {

    private final BuildingDao buildingDao;
    private final SensorDao sensorDao;
    private final WindowDao windowDao;
    private final RoomDao roomDao;
    private final HeaterDao heaterDao;

    public BuildingController(BuildingDao buildingDao, SensorDao sensorDao, WindowDao windowDao, RoomDao roomDao, HeaterDao heaterDao) {
        this.buildingDao = buildingDao;
        this.sensorDao = sensorDao;
        this.windowDao = windowDao;
        this.roomDao = roomDao;
        this.heaterDao = heaterDao;
    }

    @GetMapping
    public List<Building> findAll() {
        return buildingDao.findAll()
                .stream()
                .map(BuildingMapper::of)
                .sorted(Comparator.comparing(Building::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public Building findById(@PathVariable Long id) {
        return buildingDao.findById(id).map(BuildingMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Building> create(@RequestBody BuildingCommand building) {
        SensorEntity outsideTemperature = new SensorEntity(SensorType.TEMPERATURE, building.name() + " temperature");
        outsideTemperature.setValue(building.outsideTemperature());
        sensorDao.save(outsideTemperature);

        BuildingEntity entity = new BuildingEntity(building.name(), outsideTemperature);
        BuildingEntity saved = buildingDao.save(entity);
        return ResponseEntity.ok(BuildingMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Building> update(@PathVariable Long id, @RequestBody BuildingCommand building) {
        BuildingEntity entity = buildingDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }

        SensorEntity outsideTemperature = sensorDao.findById(entity.getOutsideTemperature().getId()).orElse(null);
        if (outsideTemperature == null) {
            return ResponseEntity.badRequest().build();
        }
        outsideTemperature.setValue(building.outsideTemperature());

        entity.setName(building.name());
        return ResponseEntity.ok(BuildingMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        sensorDao.deleteByBuildingId(id);
        windowDao.deleteWindowsByBuildingId(id);
        heaterDao.deleteHeaterByBuildingId(id);
        roomDao.deleteRoomsByBuildingId(id);
        buildingDao.deleteById(id);
    }

}
