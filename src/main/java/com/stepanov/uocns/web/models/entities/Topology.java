package com.stepanov.uocns.web.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "topology")
@NoArgsConstructor
public class Topology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "args")
    private String args;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "injection_rate")
    private Double injectionRate;

    @Column(name = "columns")
    private Integer columns;

    @Column(name = "nodes")
    private Integer nodes;

    @Column(name = "rows")
    private Integer rows;

    @OneToOne(mappedBy = "topology")
    private TopologyReport topologyReport;

    @OneToOne(mappedBy = "topology")
    private TopologyTable topologyTable;

    @OneToOne(mappedBy = "topology")
    private TopologyXml topologyXml;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
