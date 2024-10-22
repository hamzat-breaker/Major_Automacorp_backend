package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.SensorEntity;
import com.emse.spring.automacorp.model.WindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WindowDao extends JpaRepository<WindowEntity, Long>, WindowDaoCustom {

    @Query("select c from WindowEntity c where c.name=:name")
    WindowEntity findByName(@Param("name") String name);

    @Query("select c from WindowEntity c where c.windowStatus=:windowStatus")
    List<WindowEntity> findByWindowStatus(@Param("windowStatus") SensorEntity windowStatus);

    @Modifying
    @Query("delete from WindowEntity c where c.room.id = ?1")
    void deleteWindowsByRoomId(Long roomId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SensorEntity s " +
            "SET s.value = :windowValue " +
            "WHERE s.id IN (SELECT w.windowStatus.id FROM WindowEntity w WHERE w.room.id = :roomId)")
    void updateWindowStatusByRoomId(@Param("roomId") Long roomId, @Param("windowValue") Double windowValue);

    @Modifying
    @Query("delete from WindowEntity c where c.room.building.id = ?1")
    void deleteWindowsByBuildingId(Long buildingId);
}
