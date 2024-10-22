package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomDao extends JpaRepository<RoomEntity, Long> {

    @Query("select c from RoomEntity c where c.id=:id")
    RoomEntity findById(@Param("id") String id);

    @Modifying
    @Query("delete from RoomEntity c where c.building.id = ?1")
    void deleteRoomsByBuildingId(Long buildingId);

}
