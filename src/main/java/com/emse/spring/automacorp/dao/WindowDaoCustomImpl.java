package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.model.WindowEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class WindowDaoCustomImpl implements WindowDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<WindowEntity> findRoomsWithOpenWindows(Long id) {
        String jpql = "select w from WindowEntity w inner join w.windowStatus s " +
                "where w.room.id = :id and s.value > 0.0 order by w.name";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<WindowEntity> findWindowsByRoomName(String name){
        String jpql = "select w from WindowEntity w inner join w.windowStatus s " +
                "where w.room.name = :name order by w.name";
        return em.createQuery(jpql, WindowEntity.class)
                .setParameter("name", name)
                .getResultList();
    }
}
