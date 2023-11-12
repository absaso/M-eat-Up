package com.example.server.Event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepo extends JpaRepository<EventDAO, Integer> {
    List<EventDAO> findByIdResto(String idResto);
    void deleteByDateLessThan(Date startEvent);
}
