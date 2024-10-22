package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingDao extends JpaRepository<BuildingEntity, Long> {

    @Query("select c from BuildingEntity c where c.id=:id")
    BuildingEntity findById(@Param("id") String id);

}
