package com.stepanov.uocns.web.repositories;

import com.stepanov.uocns.web.models.entities.TopologyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopologyTableRepository extends JpaRepository<TopologyTable, Long> {
}
