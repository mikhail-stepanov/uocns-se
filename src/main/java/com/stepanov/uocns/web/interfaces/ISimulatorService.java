package com.stepanov.uocns.web.interfaces;

import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantRequest;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantResponse;
import com.stepanov.uocns.web.models.dtos.simulator.MeshRequest;
import com.stepanov.uocns.web.models.dtos.simulator.MeshResponse;
import com.stepanov.uocns.web.models.dtos.simulator.TorusRequest;
import com.stepanov.uocns.web.models.dtos.simulator.TorusResponse;

public interface ISimulatorService {

    CirculantResponse circulant(CirculantRequest request) throws CommonException;

    CirculantResponse optimalCirculant(CirculantRequest request) throws CommonException;

    MeshResponse mesh(MeshRequest request) throws CommonException;

    TorusResponse torus(TorusRequest request) throws CommonException;
}
