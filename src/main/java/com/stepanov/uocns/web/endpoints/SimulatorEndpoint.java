package com.stepanov.uocns.web.endpoints;

import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantRequest;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantResponse;
import com.stepanov.uocns.web.models.dtos.simulator.MeshRequest;
import com.stepanov.uocns.web.models.dtos.simulator.MeshResponse;
import com.stepanov.uocns.web.models.dtos.simulator.TorusRequest;
import com.stepanov.uocns.web.models.dtos.simulator.TorusResponse;
import com.stepanov.uocns.web.services.SimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulator")
@RequiredArgsConstructor
public class SimulatorEndpoint {

    private final SimulatorService simulatorService;

    @CrossOrigin
    @PostMapping("/circulant")
    public CirculantResponse circulant(@RequestBody CirculantRequest request) throws CommonException {
        return simulatorService.circulant(request);
    }

    @CrossOrigin
    @PostMapping("/circulant/optimal")
    public CirculantResponse optimalCirculant(@RequestBody CirculantRequest request) throws CommonException {
        return simulatorService.optimalCirculant(request);
    }

    @CrossOrigin
    @PostMapping("/mesh")
    public MeshResponse mesh(@RequestBody MeshRequest request) throws CommonException {
        return simulatorService.mesh(request);
    }

    @CrossOrigin
    @PostMapping("/circulant")
    public TorusResponse torus(@RequestBody TorusRequest request) throws CommonException {
        return simulatorService.torus(request);
    }
}
