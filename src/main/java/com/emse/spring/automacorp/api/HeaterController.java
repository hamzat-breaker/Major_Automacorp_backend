package com.emse.spring.automacorp.api;

import com.emse.spring.automacorp.dao.HeaterDao;
import com.emse.spring.automacorp.dao.RoomDao;
import com.emse.spring.automacorp.dao.SensorDao;
import com.emse.spring.automacorp.dto.Heater;
import com.emse.spring.automacorp.dto.HeaterMapper;
import com.emse.spring.automacorp.dto.Window;
import com.emse.spring.automacorp.dto.WindowMapper;
import com.emse.spring.automacorp.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/heaters")
@Transactional
public class HeaterController {
    private final HeaterDao heaterDao;
    private final SensorDao sensorDao;
    private final RoomDao roomDao;

    public HeaterController(HeaterDao heaterDao, SensorDao sensorDao, RoomDao roomDao) {
        this.heaterDao = heaterDao;
        this.sensorDao = sensorDao;
        this.roomDao = roomDao;
    }

    @GetMapping
    public List<Heater> findAll() {
        return heaterDao.findAll()
                .stream()
                .map(HeaterMapper::of)
                .sorted(Comparator.comparing(Heater::name))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public Heater findById(@PathVariable Long id) {
        return heaterDao.findById(id).map(HeaterMapper::of).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Heater> create(@RequestBody HeaterCommand heater) {
        RoomEntity room = roomDao.findById(heater.roomId()).orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }

        SensorEntity heaterStatus = new SensorEntity(SensorType.POWER, heater.name() + " power");
        heaterStatus.setValue(heater.heaterStatus());
        sensorDao.save(heaterStatus);

        HeaterEntity entity = new HeaterEntity(heater.name(), heaterStatus, room);
        HeaterEntity saved = heaterDao.save(entity);
        return ResponseEntity.ok(HeaterMapper.of(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Heater> update(@PathVariable Long id, @RequestBody HeaterCommand heater) {
        HeaterEntity entity = heaterDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }

        SensorEntity heaterStatus = sensorDao.findById(entity.getHeaterStatus().getId()).orElse(null);
        if (heaterStatus == null) {
            return ResponseEntity.badRequest().build();
        }
        heaterStatus.setValue(heater.heaterStatus());

        entity.setName(heater.name());
        entity.setPower(heater.power());
        return ResponseEntity.ok(HeaterMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        sensorDao.deleteByHeaterId(id);
        heaterDao.deleteById(id);
    }

    @PutMapping(path = "/{id}/switch")
    public ResponseEntity<Heater> update(@PathVariable Long id) {
        HeaterEntity entity = heaterDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        double newHeaterValue = 0.0;
        if (entity.getHeaterStatus().getValue()==0.0){
            newHeaterValue = 1.0;
        }
        SensorEntity heaterStatus = entity.getHeaterStatus();
        heaterStatus.setValue(newHeaterValue);
        return ResponseEntity.ok(HeaterMapper.of(entity));
    }
}
