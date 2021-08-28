package com.stepanov.uocns.web.repositories;

import com.stepanov.uocns.web.models.entities.Topology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopologyRepository extends JpaRepository<Topology, Long> {
}
