package com.stepanov.uocns.web.models.dtos.simulator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeshRequest {

    private String name;

    private String description;

    private Integer columns;

    private Integer rows;

    private Double destInjectionRate;

}
