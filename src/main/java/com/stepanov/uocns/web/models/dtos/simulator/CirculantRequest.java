package com.stepanov.uocns.web.models.dtos.simulator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CirculantRequest {

    private String name;

    private String description;

    private Integer nodes;

    private Integer firstStep;

    private Integer secondStep;

    private Double destInjectionRate;
}
