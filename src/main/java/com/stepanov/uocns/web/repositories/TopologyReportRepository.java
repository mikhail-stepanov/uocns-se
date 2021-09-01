package com.stepanov.uocns.web.repositories;

import com.stepanov.uocns.web.models.entities.TopologyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopologyReportRepository extends JpaRepository<TopologyReport, Long> {
}
