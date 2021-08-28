package com.stepanov.uocns.web.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "topology_report")
@NoArgsConstructor
public class TopologyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_topology")
    private Topology topology;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Column(name = "path")
    private String path;

}
