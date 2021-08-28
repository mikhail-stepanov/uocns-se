package com.stepanov.uocns.web.repositories;

import com.stepanov.uocns.web.models.entities.TopologyXml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopologyXmlRepository extends JpaRepository<TopologyXml, Long> {
}
