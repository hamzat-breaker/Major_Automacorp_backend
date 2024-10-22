package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.HeaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeaterDao extends JpaRepository<HeaterEntity, Long> {
    @Modifying
    @Query("delete from HeaterEntity c where c.room.id = ?1")
    void deleteHeatersByRoomId(Long roomId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SensorEntity s " +
            "SET s.value = :heaterValue " +
            "WHERE s.id IN (SELECT h.heaterStatus.id FROM HeaterEntity h WHERE h.room.id = :roomId)")
    void updateHeaterStatusByRoomId(@Param("roomId") Long roomId, @Param("heaterValue") Double heaterValue);

    @Modifying
    @Query("delete from HeaterEntity c where c.room.building.id = ?1")
    void deleteHeaterByBuildingId(Long buildingId);
}
