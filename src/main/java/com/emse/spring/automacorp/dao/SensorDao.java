package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorDao extends JpaRepository<SensorEntity, Long> {

    @Query("select c from SensorEntity c where c.id=:id")
    SensorEntity findById(@Param("id") String id);

    @Modifying
    @Query("delete from SensorEntity c where c.id IN (SELECT w.windowStatus.id FROM WindowEntity w WHERE w.id =:id)")
    default void deleteByWindowId(@Param("id") Long id) {
    }

    @Modifying
    @Query("delete from SensorEntity c where c.id IN (SELECT w.heaterStatus.id FROM HeaterEntity w WHERE w.id =:id)")
    default void deleteByHeaterId(@Param("id") Long id) {
    }

    @Modifying
    @Query("delete from SensorEntity c where c.id IN (SELECT w.windowStatus.id FROM WindowEntity w WHERE w.room.id =:id)")
    default void deleteByRoomId(@Param("id") Long id) {
    }

    @Modifying
    @Query("delete from SensorEntity c where c.id IN (SELECT w.windowStatus.id FROM WindowEntity w WHERE w.room.building.id =:id)")
    default void deleteByBuildingId(@Param("id") Long id) {
    }
}
